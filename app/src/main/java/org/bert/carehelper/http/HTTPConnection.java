package org.bert.carehelper.http;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson2.JSON;

import org.bert.carehelper.common.Constant;
import org.bert.carehelper.common.MessageHandler;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPConnection<T> {

    private String url;

    private T data;

    private String method;

    private MessageHandler handler;

    public HTTPConnection(String api, T data, String method) {
        this.url = String.format("%s%s", Constant.BASE_API, api);
        this.data = data;
        this.method = method;
        this.handler = new MessageHandler();
    }

    /**
     * parseUrlParams 参数拼接方法
     *
     * @param data 传入的参数
     * @param <T>  限定类型
     * @return 返回结果：?addr=1234&name=1234
     */
    private <T> String parseUrlParams(T data) {
        StringBuilder stringBuilder = new StringBuilder();
        // 反射获取字段并组装
        Class<?> clazz = data.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(data);
                stringBuilder
                        .append(fieldName)
                        .append("=")
                        .append(value)
                        .append("&");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        // 处理GET请求
        if (method.toLowerCase(Locale.ROOT).equals("get")) {
            Log.i("tag", "start request");
            this.url = this.url + parseUrlParams(data); // url拼接完成
            Log.i("tag", this.url);

            request = new Request.Builder()
                    .url(this.url)
                    .build();
        } else {
            RequestBody body = RequestBody.create(JSON.toJSONString(data), MediaType.get("application/json; charset=utf-8"));
            request = new Request.Builder()
                    .url(this.url)
                    .post(body)
                    .build();
        }
        try {
            Message msg = new Message();
            Bundle data = new Bundle();
            // 处理响应结果
            try (Response response = client.newCall(request).execute()) {
                String res = Objects.requireNonNull(response.body()).string();
                data.putString("value", res);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
