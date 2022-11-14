package org.bert.carehelper.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileService {
    private Context context;

    private final String TAG = "FileService";

    public FileService(Context context) {
        this.context = context;
    }

    public List<String> getFileList() {
        File file = null;
        List<String> stringList = new LinkedList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            file = Environment.getStorageDirectory();
        }
        if (file == null) {
            Log.w(TAG, "folder is null!");
            return stringList;
        }
        Log.i(TAG, String.valueOf(file.setReadable(true)));
        // 文件读取失败
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                Log.i(TAG, files[i].getName());
                stringList.add(files[i].getName());
            }
        }
        return stringList;
    }

}
