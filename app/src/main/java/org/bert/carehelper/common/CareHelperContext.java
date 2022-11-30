package org.bert.carehelper.common;


import android.content.Context;
import android.os.Handler;


public class CareHelperContext {

    private Handler networkHandler;

    private Handler webSocketHandler;

    private static CareHelperContext context;

    private CareHelperEnvironment environment = CareHelperEnvironment.getInstance();

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
}
