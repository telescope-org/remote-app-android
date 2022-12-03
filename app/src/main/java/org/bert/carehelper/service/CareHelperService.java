package org.bert.carehelper.service;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson2.JSONObject;
import com.permissionx.guolindev.PermissionX;

import org.bert.carehelper.common.CareHelperContext;
import org.bert.carehelper.common.Operation;
import org.bert.carehelper.entity.CommandResponse;
import org.bert.carehelper.entity.Poll;
import org.bert.carehelper.entity.Register;
import org.bert.carehelper.entity.RegisterResponse;
import org.bert.carehelper.http.HTTPConnection;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 后台服务
 */
public class CareHelperService extends BaseService implements Runnable {

    // PERMISSIONS 需要动态申请的权限
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
    };

    private FragmentActivity activity;

    private String TAG = this.getTag(CareHelperService.class);

    private CommandService commandService;

    private FileService fileService;

    private PhoneService phoneService;

    private LocationService locationService;

    private CareHelperContext careHelperContext = CareHelperContext.getInstance();

    private AtomicBoolean atomicBoolean;

    private Handler networkHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("CareHelperService", val);
            JSONObject jsonObject = JSONObject.parseObject(val);
            if ((Integer) jsonObject.get("code") == 200) {
                switch ((Integer) jsonObject.get("type")) {
                    case 0:
                        JSONObject response = (JSONObject) jsonObject.get("data");
                        careHelperContext.getEnvironment().setToken((String) response.get("token"));
                        Log.i("CareHelperService", "set token success!");
                        break;
                    case 1:
                        locationService.removeUpdates();
                        Log.i("CareHelperService", "location update success, remove update location func!");
                        break;
                    case 2:
                        commandService.parseCommandAndExec("");
                    default:
                        Log.i("", "");
                }
            }
            Log.i("MessageHandler", "请求结果:" + val);
        }
    };

    private Handler webSocketHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("MessageHandler", "请求结果:" + val);
        }
    };

    public CareHelperService(Context context, FragmentActivity activity) {
        super(context, activity);
        try {
            this.activity = activity;
            this.commandService = new CommandService();
            this.careHelperContext.setAndroidContext(context);
            this.careHelperContext.setNetworkHandler(networkHandler);
            this.careHelperContext.setWebSocketHandler(webSocketHandler);
            this.fileService = ((FileService) this.container.getService("FileService"));
            this.phoneService = ((PhoneService) this.container.getService("PhoneService"));
            this.locationService = ((LocationService) this.container.getService("LocationService"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            this.init();
            boolean isInitSuccess = atomicBoolean.get();
            if (isInitSuccess) {
                Log.i(TAG, "init success and wait resp!");
                this.waitResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitResponse() {
        Poll poll = new Poll(Operation.POLL,
                new Date(),
                2,
                careHelperContext.getEnvironment().getToken(),
                this.careHelperContext.getPhone()
        );
        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                careHelperContext
                        .getThreadPoolExecutor()
                        .execute(() ->
                                new HTTPConnection("/api/command/poll",
                                        poll,
                                        "GET").run()
                        );
            }
        };
        mTimer.schedule(mTimerTask, 100, 10 * 1000);
    }


    /**
     * 发起网络请求
     */
    private void registerApp(Register register) throws IOException {
        this.careHelperContext
                .getThreadPoolExecutor()
                .execute(() ->
                        new HTTPConnection("/api/phone/register", register, "GET").run()
                );
    }

    /**
     * 初始化准备工作
     * 1.读取本地文件，并加入缓存对象。
     * 2.申请相关系统权限
     * 3.向服务端申请注册（使用设备码+手机号双因子进行注册）
     */
    public void init() throws Exception {
        // 初始化读取一轮APP文件
        this.fileService.readAllAppFiles();
        atomicBoolean = new AtomicBoolean();
        // 1.注册权限系统权限
        PermissionX.init(this.activity).permissions(PERMISSIONS).request((allGranted, grantedList, deniedList) ->
        {
            if (allGranted) {
                Toast.makeText(this.context, "All permissions are granted！", Toast.LENGTH_LONG).show();
                atomicBoolean.set(true);
            } else {
                Toast.makeText(this.context, "These permissions are denied: $deniedList！", Toast.LENGTH_LONG).show();
                atomicBoolean.set(false);
            }
        });

        try {
            this.careHelperContext.setPhone(phoneService.getPhoneNumber());
            registerApp(new Register(this.careHelperContext.getPhone(), "默认地址",
                    phoneService.getDeviceId(), 0, Operation.REGISTER));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
