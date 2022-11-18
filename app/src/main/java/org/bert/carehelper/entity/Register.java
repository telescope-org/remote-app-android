package org.bert.carehelper.entity;


import org.bert.carehelper.common.Operation;

import java.util.Date;

public class Register extends RequestPackage{

    private String token;

    private String location;

    private String deviceId;

    public Register(String token, String location, String deviceId, int version, Operation operation) {
        super(operation, new Date(), version);
        this.token = token;
        this.location = location;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
