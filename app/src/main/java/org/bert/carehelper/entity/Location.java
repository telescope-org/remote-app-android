package org.bert.carehelper.entity;

import org.bert.carehelper.common.Operation;

import java.util.Date;

public class Location extends AbstractCommand  {

    private String address;

    private String phone;

    public Location(String address, Operation operation, Date createTime, int version, String token) {
        super(operation, createTime, version, token);
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
