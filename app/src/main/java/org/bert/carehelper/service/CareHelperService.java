package org.bert.carehelper.service;


import android.Manifest;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.permissionx.guolindev.PermissionX;

import org.bert.carehelper.common.API;
import org.bert.carehelper.common.CareHelperEnvironment;
import org.bert.carehelper.common.Operation;
import org.bert.carehelper.entity.CommandResponse;
import org.bert.carehelper.entity.CommonResponse;
import org.bert.carehelper.entity.Register;
import org.bert.carehelper.http.HTTPConnection;

import java.io.IOException;
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

    private CareHelperEnvironment environment = CareHelperEnvironment.getInstance();

    public CareHelperService(Context context, FragmentActivity activity) {
        super(context, activity);
        this.activity = activity;
        this.commandService = new CommandService();
        CareHelperEnvironment.getInstance().setContext(context);
        try {
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
            // 1.初始化准备工作
            this.init();
            // 2.建立websocket等待指令
            // 得到响应结果，并通过CGI回传给远端口
//            CommandResponse response = this.commandService.parseCommandAndExec("");
//            System.out.println("todo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发起网络请求
     */
    private boolean registerApp(String api, Register register) throws IOException {
        new Thread(() -> {
            Looper.prepare();
            new HTTPConnection(api, register, "GET").run();
        }).start();

//       String resp = HTTPConnection.request("GET", api, register);
//        CommonResponse commonResp = JSON.parseObject(resp, CommonResponse.class);
//        if (commonResp.getCode() == 200) {
//            return true;
//        }
//        Log.e(this.TAG, "register failed! ", new Throwable(commonResp.getMessage()));
        return false;
    }

    /**
     * websocket监听
     */
    private String webSocketListener() {
        return "";
    }

    /**
     * 初始化准备工作
     * 1.读取本地文件，并加入缓存对象。
     * 2.申请相关系统权限
     * 3.向服务端申请注册（使用设备码+手机号双因子进行注册）
     *
     * @throws Exception
     */
    public void init() throws Exception {
        // 初始化读取一轮APP文件
        this.fileService.readAllAppFiles();
        AtomicBoolean atomicBoolean = new AtomicBoolean();
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

        // 注册App
        String token = environment.getToken(phoneService.getPhoneNumber());
        try {
            boolean isRegisterApp = registerApp(API.API_PHONE + "/register", new Register(token, "默认地址",
                    phoneService.getDeviceId(), 0, Operation.REGISTER));

            atomicBoolean.set(isRegisterApp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 确定所有启动流程都完成之后再返回
        int tryTimes = 0;
        while (true) {
            boolean isInitSuccess = atomicBoolean.get();
            // 30s如果还没启动，就设置为失败。
            if (tryTimes > 10) {
                Log.e(TAG, "init failed!");
                return;
            }
            if (isInitSuccess) {
                Log.i(TAG, "init success!");
                return;
            }
            tryTimes++;
        }

    }
}
