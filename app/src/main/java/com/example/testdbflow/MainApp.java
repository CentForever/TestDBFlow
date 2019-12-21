package com.example.testdbflow;

import android.app.Application;

import net.sqlcipher.database.SQLiteDatabase;

public class MainApp extends Application {

    public static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        SQLiteDatabase.loadLibs(this);
    }

}
