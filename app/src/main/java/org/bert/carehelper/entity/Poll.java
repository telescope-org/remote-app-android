package org.bert.carehelper.entity;

import org.bert.carehelper.common.Operation;

import java.util.Date;

public class Poll extends AbstractCommand{

    private String phone;

    public Poll(Operation operation, Date createTime, int version, String token, String phone) {
        super(operation, createTime, version, token);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
