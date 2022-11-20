package org.bert.carehelper.service;


import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class BaseService {

    protected Context context;

    protected PhoneService phoneManagerService;
    protected FileService fileService;
    protected AppService appService;
    protected LocationService locationService;
    protected CommandService commandService;

    public BaseService(Context context, FragmentActivity activity) {
        this.context = context;
        this.phoneManagerService = new PhoneService(context);
        this.fileService = new FileService(context, activity);
        this.appService = new AppService(context);
        this.locationService = new LocationService(context);
        this.commandService = new CommandService(context);
    }

    public String getTag(Class clazz) {
        return clazz.getName();
    }
}
