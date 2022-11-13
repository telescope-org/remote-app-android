package org.bert.carehelper.http;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.bert.carehelper.common.WsStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WebSocketManager {

    private volatile static WebSocketManager wsManger;

    private WebSocket ws;

    private WebSocketManager() {
    }

    public static WebSocketManager getWsManger() {
        if (wsManger == null) {
            synchronized (WebSocketManager.class) {
                if (wsManger == null) {
                    wsManger = new WebSocketManager();
                }
            }
        }
        return wsManger;
    }
    /**
     * 连接方法 这里要判断是否登录 此处省略
     */
    public void connect() {
        //WEB_SOCKET_API 是连接的url地址，
        // CONNECT_TIMEOUT是连接的超时时间 这里是 5秒
        try {
            ws = new WebSocketFactory().createSocket("", 9999)
                    //设置帧队列最大值为5
                    .setFrameQueueSize(5)
                    //设置不允许服务端关闭连接却未发送关闭帧
                    .setMissingCloseFrameAllowed(false)
                    //添加回调监听
                    .addListener(new WsListener())
                    //异步连接
                    .connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setStatus(WsStatus.CONNECTING);
    }

    private void setStatus(WsStatus status) {
        
    }

    private static class WsListener extends WebSocketAdapter {

        private final String TAG = "WsListener";

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            Log.d(TAG, "onConnected: 连接成功");
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            Log.d(TAG, "onConnectError: 连接失败");
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                   WebSocketFrame clientCloseFrame,
                                   boolean closedByServer) throws Exception {
            Log.d(TAG, "onDisconnected: 断开连接");

        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            Log.d(TAG, "onTextMessage: 收到消息:" + text);
        }
    }
}

