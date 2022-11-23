package org.bert.carehelper.service;


import android.util.Log;


import com.alibaba.fastjson2.JSON;

import org.bert.carehelper.common.ServiceContainer;
import org.bert.carehelper.entity.Command;
import org.bert.carehelper.common.CommandType;
import org.bert.carehelper.entity.CommandResponse;

import java.util.Map;

/**
 * 指令解析
 */
public class CommandService  {

    private ServiceContainer container = ServiceContainer.getInstance();

    private FileService fileService;

    private LocationService locationService;

    private PhoneService phoneService;

    private AppService appService;

    public CommandService() {
        try {
            this.fileService = ((FileService)this.container.getService("FileService"));
            this.locationService = ((LocationService) this.container.getService("LocationService"));
            this.phoneService = ((PhoneService) this.container.getService("PhoneService"));
            this.appService = ((AppService) this.container.getService("AppService"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析并执行命令
     * @param command 命令字符
     */
    public CommandResponse parseCommandAndExec(String command) {
        Command cmd = JSON.parseObject(command, Command.class);
        CommandResponse response = null;
        switch (cmd.getType()) {
            case CommandType.FILE_READER:
                response = this.fileService.doCommand(cmd.getContent());
                break;
            case CommandType.LOCATION:
                response = this.locationService.doCommand(cmd.getContent());
                break;
            case CommandType.PHONE:
                response = this.phoneService.doCommand(cmd.getContent());
                break;
            default:
                Log.e("CommandService", "type error!");
                break;
        }
        return response;
    }

}
