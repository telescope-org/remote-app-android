package org.bert.carehelper.http;

import org.bert.carehelper.common.Constant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HTTPConnection {

    public static <T> void request(String method, String api, Map<String, T> data) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = String.format("%s%s", Constant.BASE_API, api); // 基础url请求
        // 处理GET请求
        if (method.toLowerCase(Locale.ROOT).equals("get")) {
            url = url + parseUrlParams(data); // url拼接完成
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println(Objects.requireNonNull(response.body()).string());
            }
        }
    }

    private static <T> String parseUrlParams(Map<String, T> data) {
        StringBuilder stringBuilder = new StringBuilder();
        // 无参数兜底
        if (data.size() == 0) {
            return "";
        }
        stringBuilder.append("?");
        for (String key : data.keySet()) {
            stringBuilder
                    .append(key)
                    .append("=")
                    .append(data.get(key))
                    .append("&");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }
}
