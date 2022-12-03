package org.bert.carehelper.common;


import android.content.Context;
import android.os.Handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CareHelperContext {

    private Handler networkHandler;

    private Handler webSocketHandler;

    private static CareHelperContext context;

    private String phone;

    private CareHelperEnvironment environment = CareHelperEnvironment.getInstance();

    final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,5,1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100));

    private Context androidContext;

    public static CareHelperContext getInstance() {
        if (context == null) {
            synchronized (CareHelperContext.class) {
                if (context == null) {
                    context = new CareHelperContext();
                }
            }
        }
        return context;
    }

    public Context getAndroidContext() {
        return androidContext;
    }

    public void setAndroidContext(Context androidContext) {
        this.androidContext = androidContext;
    }


    public CareHelperEnvironment getEnvironment() {
        return this.environment;
    }

    public Handler getNetworkHandler() {
        return networkHandler;
    }

    public void setNetworkHandler(Handler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public Handler getWebSocketHandler() {
        return webSocketHandler;
    }

    public void setWebSocketHandler(Handler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
