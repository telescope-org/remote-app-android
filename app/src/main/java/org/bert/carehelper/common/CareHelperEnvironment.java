package org.bert.carehelper.common;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import java.util.Objects;

public class CareHelperEnvironment {

    private static CareHelperEnvironment instance;

    private FragmentActivity activity;

    private Integer requestCode;

    private Context context;

    private CareHelperEnvironment() {}

    public static CareHelperEnvironment getInstance() {
        // 先判断实例是否存在，若不存在再对类对象进行加锁处理
        if (instance == null) {
            synchronized (CareHelperEnvironment.class) {
                if (instance == null) {
                    instance = new CareHelperEnvironment();
                }
            }
        }
        return instance;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public Integer getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(Integer requestCode) {
        this.requestCode = requestCode;
    }

    public boolean isSameRequestCode(Integer requestCode) {
        return Objects.equals(requestCode, this.requestCode);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
