package com.example.testdbflow;

import android.app.Application;
import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.dianping.logan.OnLoganProtocolStatus;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import net.sqlcipher.database.SQLiteDatabase;
import java.io.File;

public class MainApp extends Application {
    private static final String TAG = MainApp.class.getName();
    private static final String FILE_NAME = "logan_v1";

    public static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        SQLiteDatabase.loadLibs(this);
        initLogan();
        initMarsXLog();
    }

    private void initLogan() {
        LoganConfig config = new LoganConfig.Builder()
                .setCachePath(getApplicationContext().getFilesDir().getAbsolutePath())
                .setPath(getApplicationContext().getExternalFilesDir(null).getAbsolutePath()
                        + File.separator + FILE_NAME)
                .setEncryptKey16("0123456789012345".getBytes())
                .setEncryptIV16("0123456789012345".getBytes())
                .build();
        Logan.init(config);
        Logan.setDebug(true);
        Logan.setOnLoganProtocolStatus(new OnLoganProtocolStatus() {
            @Override
            public void loganProtocolStatus(String cmd, int code) {
                Log.d(TAG, "clogan > cmd : " + cmd + " | " + "code : " + code);
            }
        });

    }

    private void initMarsXLog(){
        //https://www.jianshu.com/p/1bee0dc5d0d9
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
        final String fliesDir = this.getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
        final String xlogPath = fliesDir + "/MarsXLog/log";
        final String xlogCachePath = this.getFilesDir() + "/xlog";

        //init xlog
        if (BuildConfig.DEBUG) {
            Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, xlogCachePath, xlogPath, "TestDbFlow", 7, "");
            Xlog.setConsoleLogOpen(true);
        } else {
            Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, xlogCachePath, xlogPath, "TestDbFlow", 7, "");
            Xlog.setConsoleLogOpen(false);
        }
        Log.setLogImp(new Xlog());
        Log.e("SYS_INFO",Log.getSysInfo());
    }
}
