package org.bert.carehelper.service;


import android.content.Context;

public class BaseService {

    private Context context;

    public BaseService(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
