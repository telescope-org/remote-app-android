package org.bert.carehelper.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * https://github.com/wangyu1920/PubgBattleInfoGetter
 * 来网络某大神
 */
public class FileUrlUtils {
    /**
     * 根据指定文件路径获取文件输出流
     *
     * @param context 上下文
     * @param path    路径
     * @return 输出流
     * @throws FileNotFoundException 没找到文件或者这是文件夹时抛出异常
     */
    public static OutputStream getOutputStream(Context context, String path) throws FileNotFoundException {
        if (!(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context,
                "android.permission.WRITE_EXTERNAL_STORAGE"))) {
            throw new FileNotFoundException("没得读写权限");
        }
        if (path.contains("Android/data/")) {
            if (!isGrant$Data(context)) {
                throw new FileNotFoundException("没得data权限");
            }
            return getOutputStream$Data(context, path);
        } else {
            File file = new File(path.replace("/document/primary:", "/storage/emulated/0/"));
            return new FileOutputStream(file);
        }
    }

    /**
     * 根据指定文件路径获取文件输入流
     *
     * @param context 上下文
     * @param path    路径
     * @return 输入流
     * @throws FileNotFoundException 没找到文件或者这是文件夹时抛出异常
     */
    public static InputStream getInputStream(Context context, String path) throws FileNotFoundException {
        if (!(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context,
                "android.permission.WRITE_EXTERNAL_STORAGE"))) {
            throw new FileNotFoundException("没得读写权限");
        }
        if (path.contains("Android/data/")) {
            if (!isGrant$Data(context)) {
                throw new FileNotFoundException("没得data权限");
            }
            return getInputStream$Data(context, path);
        } else {
            File file = new File(path.replace("/document/primary:", "/storage/emulated/0/"));
            return new FileInputStream(file);
        }
    }


    /**
     * 根据指定文件夹路径，获得路径下的文件列表
     *
     * @param context 上下文
     * @param path    路径
     * @return Object[] DocumentFile或者File，后续需要做强转
     * @throws FileNotFoundException 没找到文件夹或者这不是文件夹时抛出异常
     */
    public static Object[] getFileLists(Context context, String path) throws FileNotFoundException {
        if (!(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context,
                "android.permission.WRITE_EXTERNAL_STORAGE"))) {
            throw new FileNotFoundException("没得读写权限");
        }
        if (path.contains("Android/data/")) {
            if (!isGrant$Data(context)) {
                throw new FileNotFoundException("没得data权限");
            }
            DocumentFile documentFile = getDocumentFile$Data(context, path);
            if (documentFile.isDirectory()) {
                return documentFile.listFiles();
            }
        } else {
            File file = new File(path.replace("/document/primary:", "/storage/emulated/0/"));
            if (file.isDirectory()) {
                return file.listFiles();
            }
        }
        throw new FileNotFoundException("该路径不是文件夹");
    }

    //将输入流写入输出流
    public static boolean copy(@NonNull InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] bytes = new byte[10];
            int len;
            while (-1 != (len = inputStream.read(bytes))) {
                outputStream.write(bytes,0,len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //直接获取data权限
    public static void startForRoot$Data(Activity context, int REQUEST_CODE_FOR_DIR) {
        String uri = changeToUri$Data(Environment.getExternalStorageDirectory().getPath());
        uri = uri + "/document/primary%3A" + Environment.getExternalStorageDirectory().getPath().replace("/storage/emulated/0/", "").replace("/", "%2F");
        Uri parse = Uri.parse(uri);
        DocumentFile documentFile = DocumentFile.fromTreeUri(context, parse);
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        assert documentFile != null;
        intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentFile.getUri());
        context.startActivityForResult(intent1, REQUEST_CODE_FOR_DIR);

    }

    //安卓11及以上获取所有文件访问权限
    public static void startForRoot$AllFile(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        activity.startActivity(intent);
    }

    //获取读写权限
    public static void startForRoot$File(Activity activity,int REQUEST_CODE) {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE );
        }
    }

    //判断DocumentFile是否可用
    public static boolean fileCanUse(DocumentFile documentFile) {
        if (documentFile == null) {
            Log.e("文件夹错误", "文件夹==null");
            return false;
        }
        if (documentFile.exists()) {
            if (!documentFile.canRead()) {
                Log.e("读文件夹错误", "文件夹不可读");
                return false;
            }
            if (!documentFile.canWrite()) {
                Log.e("读文件夹错误", "文件夹不可写");
                return false;
            }
            Log.d("读文件夹成功", "文件夹可读写");
            return true;
        } else {
            Log.e("读文件夹错误", "文件夹不存在");
            return false;
        }
    }

//    *******************工具方法***********************************************************************

    protected static OutputStream getOutputStream$Data(Context context, String path) throws FileNotFoundException {
        DocumentFile documentFile=getDocumentFile$Data(context, path);
        if (documentFile.isDirectory()) {
            throw new FileNotFoundException("这是一个文件夹！");
        }
        return context.getContentResolver().openOutputStream(documentFile.getUri());
    }

    protected static InputStream getInputStream$Data(Context context, String path) throws FileNotFoundException {
        DocumentFile documentFile=getDocumentFile$Data(context, path);
        if (documentFile.isDirectory()) {
            throw new FileNotFoundException("这是一个文件夹！:"+changeToUri$Data(path));
        }
        return context.getContentResolver().openInputStream(documentFile.getUri());
    }


    //根据路径获得document文件
    public static DocumentFile getDocumentFile$Data(Context context, String path) throws FileNotFoundException {
        DocumentFile documentFile = DocumentFile.fromSingleUri(context, Uri.parse(changeToUri$Data(path)));
        DocumentFile documentFile1;
        if (fileCanUse(documentFile)) {
            assert documentFile != null;
            documentFile1 = DocumentFile.fromTreeUri(context, documentFile.getUri());
        } else {
            throw new FileNotFoundException("没有找到该文件:"+changeToUri$Data(path));
        }


        return documentFile1;
    }


    protected static String changeToUri$Data(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        @SuppressLint("SdCardPath") String path2 = path.replace("/tree/primary:Android/data/document/primary:","").replace("/document/primary:", "/storage/emulated/0/").replace("/storage/emulated/0/", "").replace("/sdcard/", "").replace("/", "%2F");
        return "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A" + path2;
    }

//    ********************关于授权**********************************************************

    //判断是否已经获取了Data权限
    public static boolean isGrant$Data(Context context) {
        for (UriPermission persistedUriPermission : context.getContentResolver().getPersistedUriPermissions()) {
            System.out.println(persistedUriPermission.getUri().toString()+"******************************************");
            if (persistedUriPermission.isReadPermission() && persistedUriPermission.getUri().toString()
                    .equals("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata")) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static boolean isGrant$AllFiles() {
        return Environment.isExternalStorageManager();
    }

    //判断是否已经获取了读写权限
    public static boolean isGrant$File(Context context) {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context,
                "android.permission.WRITE_EXTERNAL_STORAGE");
    }
}

