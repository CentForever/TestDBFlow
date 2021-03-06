package com.example.testdbflow.xlog;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.testdbflow.MainApp;
import com.example.testdbflow.db.WcdbEncryptedDBHelper;
import com.tencent.mars.xlog.Log;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZipLogFile {
    private static String TAG = "ZipLogFile";

    /**
     * 压缩日志文件 返回日志文件路径
     * <p>
     * 建议：异步任务中执行
     *
     * @param context
     * @return
     */
    public static File zipLogFiles(Context context) {
        if (context == null) {
            return null;
        }
        Log.appenderFlush(true);
        Log.appenderClose();
        final String fliesDir = context.getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
        final String xlogPath = fliesDir + "/MarsXLog/log";
        final String xlogCachePath = context.getFilesDir() + "/xlog";
        final String databases;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            databases = context.getDataDir() + "/databases";
        }else{
            databases ="/data/data/"+context.getPackageName()+"/databases";
        }
        // /data/user/0/com.example.testdbflow/databases
        if (!TextUtils.isEmpty(fliesDir)) {
            // 当前时间作为文件名
            final String fileName = "Xlog.zip";
            // 最终文件路径
            File zipFile = new File(FileUtils.getAppCachePath(context), fileName);
            List<String> filePaths = new ArrayList<>();
            filePaths.add(xlogPath);
            filePaths.add(xlogCachePath);
            filePaths.add(databases);
            boolean flag = FileUtils.exportZipFromPaths(filePaths, zipFile);
            if (flag) {
                return zipFile;
            }
        }
        return null;
    }

    public static void test(){
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                Log.e("mgg","加密开始");
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                ZipLogFile.zipLogFiles(MainApp.application);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean cursor) {
                Log.e("mgg","加密完成");
            }
        }.execute();
    }


}