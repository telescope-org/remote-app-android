package org.bert.carehelper.service;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppService {


    private Context context;

    private ContentResolver cr = null;

    public AppService(Context context) {
        this.context = context;
        if (context != null) {
            this.cr = this.context.getContentResolver();
        } else {
            Log.e("PhoneService", "context is null!");
        }
    }

    public List<String> getPkgListNew() {
        List<String> packages = new ArrayList<String>();
        try {
            List<ApplicationInfo> packageInfos = context.getPackageManager().getInstalledApplications(0);
            for (ApplicationInfo info : packageInfos) {
                String pkg = info.packageName;
                packages.add(pkg);
            }
        } catch (Throwable t) {
            t.printStackTrace();;
        }
        return packages;
    }

    public boolean unInstallAppSafety(String packageName) {
        Uri uri = Uri.fromParts("package", packageName, null);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        this.context.startActivity(intent);
        return true;
    }

    public boolean unInstallAppForce(String packageName) {
        // 静默卸载
        return true;
    }

}
