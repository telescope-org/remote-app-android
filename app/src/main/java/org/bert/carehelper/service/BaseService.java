package org.bert.carehelper.service;


import android.content.Context;

public class BaseService {

    private Context context;

    protected PhoneService phoneManagerService;
    protected FileService fileService;
    protected AppService appService;
    protected LocationService locationService;
    protected CommandService commandService;

    public BaseService(Context context) {
        this.context = context;
        this.phoneManagerService = new PhoneService(context);
        this.fileService = new FileService(context);
        this.appService = new AppService(context);
        this.locationService = new LocationService(context);
        this.commandService = new CommandService(context);
    }

    public Context getContext() {
        return context;
    }

    public String getTag(Class clazz) {
        return clazz.getName();
    }
}
