package org.bert.carehelper.service;

import android.content.Context;

public class CommandService  {

    private Context context;

    public CommandService(Context context) {
        this.context = context;
    }

    public void parseCommandAndExec(String command) {
        // todo 服务器远程命令设计
    }
}
