package org.bert.carehelper.service;


import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import org.bert.carehelper.common.ServiceContainer;

public class BaseService {

    protected Context context;

    protected ServiceContainer container = ServiceContainer.getInstance();

    public BaseService(Context context, FragmentActivity activity) {
        this.context = context;
        // 注入这些服务
        this.container
                .addService("PhoneService", new PhoneService(context))
                .addService("FileService", new FileService(context, activity))
                .addService("AppService", new AppService(context))
                .addService("LocationService", new LocationService(context));
    }

    public String getTag(Class clazz) {
        return clazz.getName();
    }
}
