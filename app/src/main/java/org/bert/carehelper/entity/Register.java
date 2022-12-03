package org.bert.carehelper.entity;


import org.bert.carehelper.common.Operation;

import java.util.Date;

public class Register extends AbstractCommand {

    private String phone;

    private String location;

    private String deviceId;

    public Register(String phone, String location, String deviceId, int version, Operation operation) {
        super(operation, new Date(), version, "");
        this.phone = phone;
        this.location = location;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
