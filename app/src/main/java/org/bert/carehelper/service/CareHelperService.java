package org.bert.carehelper.service;


import android.Manifest;
import android.content.Context;

import com.alibaba.fastjson.JSON;

import org.bert.carehelper.common.API;
import org.bert.carehelper.common.Constant;
import org.bert.carehelper.common.Operation;
import org.bert.carehelper.entity.Command;
import org.bert.carehelper.entity.Register;
import org.bert.carehelper.http.HTTPConnection;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 后台服务
 */
public class CareHelperService extends BaseService implements Runnable {


    private AtomicInteger version = new AtomicInteger(0);


    public CareHelperService(Context context) {
        super(context);

    }

    @Override
    public void run() {
        try {
            this.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer init() throws IOException {
        // 初始化请求注册

        String resp = HTTPConnection.request(Constant.GET, API.API_PHONE,
                new Register("",
                        this.locationService.getLocation().getAddress(),
                        "",
                        version.getAndIncrement(),
                        Operation.REGISTER
                ));

        Object obj = JSON.parse(resp);

        return 0;
    }
}
