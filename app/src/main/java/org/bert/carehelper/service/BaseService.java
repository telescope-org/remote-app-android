package org.bert.carehelper.service;


import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import org.bert.carehelper.annotation.Load;
import org.bert.carehelper.common.ServiceContainer;

public class BaseService {

    protected Context context;

    @Load(value = "PhoneService")
    protected PhoneService phoneManagerService;
    @Load(value = "FileService")
    protected FileService fileService;
    @Load(value = "AppService")
    protected AppService appService;
    @Load(value = "LocationService")
    protected LocationService locationService;

    public BaseService(Context context, FragmentActivity activity) {
        this.context = context;
    }

    public String getTag(Class clazz) {
        return clazz.getName();
    }
}
