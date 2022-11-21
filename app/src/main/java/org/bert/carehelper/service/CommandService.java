package org.bert.carehelper.service;


import android.util.Log;

import com.alibaba.fastjson2.JSON;

import org.bert.carehelper.entity.Command;
import org.bert.carehelper.common.CommandType;


public class CommandService  {

    public static void parseCommandAndExec(String command) {
        Command cmd = JSON.parseObject(command, Command.class);
        switch (cmd.getType()) {
            case CommandType.QQ_FILE_READER:
                break;
            case CommandType.WE_FILE_READER:
                break;
            default:
                Log.i("", "");
        }
    }
}
