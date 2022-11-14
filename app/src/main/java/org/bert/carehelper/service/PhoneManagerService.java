package org.bert.carehelper.service;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PhoneManagerService {

    private Context context;

    private ContentResolver cr;

    public PhoneManagerService(Context context) {
        this.cr = this.context.getContentResolver();
        this.context = context;
    }

    /**
     * 获取电话号码
     * @return
     */
    public String getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) this.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String PhoneNumber = null;
        if (ActivityCompat.checkSelfPermission(this.context,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.context,
                Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.context, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
            return "000-0000-0000";
        }
        PhoneNumber = telephonyManager.getLine1Number();//返回设备的电话号码
        return PhoneNumber;
    }


    /**
     * 获取短信列表
     */
    public List<Map<String, String>> getPhoneMessageList() {
        List<Map<String, String>> list = new LinkedList<>();
        Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = this.cr.query(SMS_INBOX, projection, null, null, "date desc");
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
            Map<String, String> map = new HashMap<String, String>();
            map.put("phone", number);
            map.put("message", body);
            list.add(map);
        }
        return list;
    }

    /**
     * 获取通讯记录
     */
    public void getPhoneRecords() {

    }

    /**
     * 获取联系人列表
     */
    public void getContactList() {

    }}
