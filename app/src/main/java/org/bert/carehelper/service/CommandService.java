package org.bert.carehelper.service;


import android.util.Log;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import org.bert.carehelper.common.ServiceContainer;
import org.bert.carehelper.entity.Command;
import org.bert.carehelper.common.CommandType;
import org.bert.carehelper.entity.CommandResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指令解析
 */
public class CommandService {

    private ServiceContainer container = ServiceContainer.getInstance();

    private FileService fileService;

    private LocationService locationService;

    private PhoneService phoneService;

    private AppService appService;

    public CommandService() {
        try {
            this.fileService = ((FileService) this.container.getService("FileService"));
            this.locationService = ((LocationService) this.container.getService("LocationService"));
            this.phoneService = ((PhoneService) this.container.getService("PhoneService"));
            this.appService = ((AppService) this.container.getService("AppService"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseCommandsAndExec(List<Object> objectList) {
        JSONObject vals = (JSONObject) objectList.get(0);
        List<Object> cmds = new ArrayList<>(vals.values());
        for (Object obj: cmds) {
            this.parseCommandAndExec((String)obj);
            Log.i("CommandService", "wait 10 sec to do next task!");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 解析并执行命令
     *
     * @param command 命令字符
     */
    public void parseCommandAndExec(String command) {
        Log.i("command", "doCommand: " + command);
        String type = command.split(":")[0];
        switch (type) {
            case CommandType.FILE:
                this.fileService.doCommand(command);
                break;
            case CommandType.LOCATION:
                this.locationService.doCommand(command);
                break;
            case CommandType.PHONE:
                this.phoneService.doCommand(command);
                break;
            default:
                Log.e("CommandService", "type error!");
                break;
        }
    }
}
