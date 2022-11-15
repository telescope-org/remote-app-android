package org.bert.carehelper.entity;

import android.provider.CallLog;

import java.util.HashMap;
import java.util.Map;

public class CallLogInfo {
    private String number;

    private long date;

    private int type;

    public CallLogInfo(String number, long date, int type) {
        this.number = number;
        this.date = date;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getType() {
        switch (this.type)
        {
            case CallLog.Calls.INCOMING_TYPE:
                return "呼入";
            case CallLog.Calls.OUTGOING_TYPE:
                return  "呼出";
            case CallLog.Calls.MISSED_TYPE:
                return  "未接";
            default:
               return  "挂断";
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CallLogInfo{" +
                "number='" + number + '\'' +
                ", date=" + date +
                ", type=" + getType() +
                '}';
    }
}
