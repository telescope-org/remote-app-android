package org.bert.carehelper.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class MessageHandler extends Handler {
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        Bundle data = msg.getData();
        String val = data.getString("value");
        Log.i("MessageHandler","请求结果:" + val);
    }
}
