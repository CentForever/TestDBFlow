package com.example.testdbflow;

import android.app.Application;
import android.util.Log;
import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.dianping.logan.OnLoganProtocolStatus;
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
}
