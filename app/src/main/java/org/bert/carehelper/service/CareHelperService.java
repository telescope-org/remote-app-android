package org.bert.carehelper.service;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.permissionx.guolindev.PermissionX;

import org.bert.carehelper.entity.Command;

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

    public CareHelperService(Context context, FragmentActivity activity) {
        super(context, activity);
        this.activity = activity;
        this.commandService = new CommandService();
        try {
            this.fileService = ((FileService)this.container.getService("FileService"));
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

            System.out.println("todo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化准备工作
     * 1.读取本地文件，并加入缓存对象。
     * 2.申请相关系统权限
     * 3.向服务端申请注册（使用设备码+手机号双因子进行注册）
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
        // TODO make a http request to regist user！


        // 确定所有启动流程都完成之后再返回
        int tryTimes = 0;
        while (true) {
            boolean isInitSuccess = atomicBoolean.get();
            // 30s如果还没启动，就设置为失败。
            if (tryTimes > 10) {
                Log.e(TAG, "init failed!");
            }
            if (isInitSuccess) {
                Log.i(TAG, "init success!");
                return;
            }
            tryTimes++;
            // 3s 检查一次
            Thread.sleep(3000);
        }

    }
}
