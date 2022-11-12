package org.bert.carehelper.http;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SocketConnection {

    private Socket socket;

    private String host = "127.0.0.1";

    private int port = 9999;

    private String TAG = "SocketConnection";

    private ExecutorService mThreadPool;

    public void run() {
        this.connectSocket();
    }

    private void connectSocket() {
        mThreadPool.execute(() -> {
            try {
                socket = new Socket(host, port);
                if (socket.isConnected()) {
                    Log.i(TAG, "链接成功！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void receiveMessage() {
        mThreadPool.execute(() -> {
            // 读取socket结果
        });
    }
}
