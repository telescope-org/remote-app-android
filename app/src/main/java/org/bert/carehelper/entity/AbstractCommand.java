package org.bert.carehelper.entity;

import org.bert.carehelper.common.Operation;

import java.util.Date;

public abstract class AbstractCommand {
    private Operation operation;

    private Date createTime;

    private int version;


    public AbstractCommand(Operation operation, Date createTime, int version) {
        this.operation = operation;
        this.createTime = createTime;
        this.version = version;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
