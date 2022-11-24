package org.bert.carehelper.http;

import com.alibaba.fastjson2.JSON;

import org.bert.carehelper.common.Constant;
import org.bert.carehelper.entity.AbstractCommand;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPConnection<T> {

    public static <T> String request(String method, String api, T data) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        String url = String.format("%s%s", Constant.BASE_API, api); // 基础url请求
        // 处理GET请求
        if (method.toLowerCase(Locale.ROOT).equals("get")) {
            url = url + parseUrlParams(data); // url拼接完成
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            RequestBody body = RequestBody.create(JSON.toJSONString(data), MediaType.get("application/json; charset=utf-8"));
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }
        // 处理响应结果
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }


    /**
     * parseUrlParams 参数拼接方法
     * @param data 传入的参数
     * @param <T> 限定类型
     * @return 返回结果：?addr=1234&name=1234
     */
    private static <T> String parseUrlParams(T data) {
        StringBuilder stringBuilder = new StringBuilder();

        // TODO 反射获取字段参数，然后进行组装
        // 无参数兜底
//        if (data.size() == 0) {
//            return "";
//        }
//        stringBuilder.append("?");
//        for (String key : data.keySet()) {
//            stringBuilder
//                    .append(key)
//                    .append("=")
//                    .append(data.get(key))
//                    .append("&");
//        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }
}
