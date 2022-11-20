package org.bert.carehelper.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;

import org.bert.carehelper.common.CareHelperEnvironment;
import org.bert.carehelper.common.FileUrlUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class FileService {
    private Context context;

    private final String TAG = "FileService";

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";

    private FragmentActivity activity;

    protected Map<String, DocumentFile> fileMap;

    public FileService(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;

        if (!FileUrlUtils.isGrant$File(this.context)) {
            // 获取读写权限
            FileUrlUtils.startForRoot$Data(this.activity, 1);
            // 设置返回Code
            CareHelperEnvironment.getInstance().setRequestCode(1);
            FileUrlUtils.startForRoot$File(this.activity, 1);
        }

        this.fileMap = new HashMap<>();
    }

    /**
     * 获取所有的APP对象
     */
    public void readAllAppFiles() throws FileNotFoundException {
        DocumentFile[] files = (DocumentFile[]) FileUrlUtils.getFileLists(CareHelperEnvironment.getInstance().getActivity(), path);
        this.fileMap = parseAppFiles(files);
    }

    /**
     * 读取QQ下载的文件目录
     * @param target 包名（例如：com.tencent.qq）
     */
    public Map<String, DocumentFile> getQQRecvFiles(String target) {
        DocumentFile qqFile = this.fileMap.get(target);
        if (qqFile != null) {
            DocumentFile[] appFiles = qqFile.listFiles();
            Map<String, DocumentFile> qqFileMap = parseAppFiles(appFiles);
            DocumentFile tencentFile = qqFileMap.get("Tencent");
            if (tencentFile != null) {
                Map<String, DocumentFile> tencentFileMap = parseAppFiles(tencentFile.listFiles());
                DocumentFile fileRecv = tencentFileMap.get("QQfile_recv");
                if (fileRecv != null) {
                    return parseAppFiles(fileRecv.listFiles());
                }
            }
        }
        return null;
    }

    /**
     * 读取wechat文件列表
     * @param target 包名（例如：com.tencent.qq）
     */
    public Map<String, DocumentFile> getWechatFiles(String target) {
        DocumentFile wechatFolder = this.fileMap.get(target);
        if (wechatFolder != null) {
            DocumentFile[] appFiles = wechatFolder.listFiles();
            Map<String, DocumentFile> wechatFileMap = parseAppFiles(appFiles);
            DocumentFile msgFile = wechatFileMap.get("MicroMsg");
            if (msgFile != null) {
                Map<String, DocumentFile> msgFileMap = parseAppFiles(msgFile.listFiles());
                DocumentFile downloadFile = msgFileMap.get("Download");
                if (downloadFile != null) {
                    return parseAppFiles(downloadFile.listFiles());
                }
            }
        }
        return null;
    }

    private Map<String, DocumentFile> parseAppFiles(DocumentFile[] appFiles) {
        Map<String, DocumentFile> fileMap = new HashMap<>();
        for (int i = 0; i < appFiles.length; i++) {
            DocumentFile file = appFiles[i];
            fileMap.put(file.getName(), file);
        }
        return fileMap;
    }

}
