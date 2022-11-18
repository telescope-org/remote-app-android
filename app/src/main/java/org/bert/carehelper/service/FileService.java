package org.bert.carehelper.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;


public class FileService {
    private Context context;

    private final String TAG = "FileService";

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/";

    public FileService(Context context) {
        this.context = context;
    }

    public void readFile() {
        System.out.println(path);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "apply permission refuse!!!");
            return;
        }
        File file = new File(this.path);
        System.out.println(file.canRead());
    }

    public boolean checkPermissions() {
        return false;
    }

}
