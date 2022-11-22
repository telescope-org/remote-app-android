package org.bert.carehelper.service;


import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.alibaba.fastjson2.JSON;

import org.bert.carehelper.common.ServiceContainer;
import org.bert.carehelper.entity.Command;
import org.bert.carehelper.common.CommandType;

import java.util.Map;

/**
 * 指令解析
 */
public class CommandService  {

    private ServiceContainer container = ServiceContainer.getInstance();

    private FileService fileService;

    public CommandService() {
        try {
            this.fileService = ((FileService)this.container.getService("FileService"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseCommandAndExec(String command) throws Exception {
        Command cmd = JSON.parseObject(command, Command.class);
        switch (cmd.getType()) {
            case CommandType.QQ_FILE_READER:
                Map<String, DocumentFile> map = this.fileService.getQQRecvFiles();
                break;
            case CommandType.WE_FILE_READER:
                break;
            default:
                Log.i("", "");
        }
    }

}
