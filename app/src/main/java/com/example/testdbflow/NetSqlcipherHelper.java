package com.example.testdbflow;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import java.io.File;
import java.io.IOException;

public class NetSqlcipherHelper {

    public static final String DATABASE_NAME = "NetSqlcipher.db";
    private static void ConvertNormalToSQLCipheredDB(Context context, String startingFileName, String endingFileName)
            throws IOException {
        File mStartingFile = context.getDatabasePath(startingFileName);
        if (!mStartingFile.exists()) {
            return;
        }
        File mEndingFile = context.getDatabasePath(endingFileName);
        mEndingFile.delete();
        SQLiteDatabase database = null;
        try {

            database = SQLiteDatabase.openOrCreateDatabase(mStartingFile,
                    "", null);
            database.rawExecSQL(String.format(
                    "ATTACH DATABASE '%s' AS encrypted KEY '%s'",
                    mEndingFile.getAbsolutePath(), "mgg"));
            database.rawExecSQL("select sqlcipher_export('encrypted')");
            database.rawExecSQL("DETACH DATABASE encrypted");
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database.isOpen())
                database.close();
            //mStartingFile.delete();
        }
    }

    public static void test(){
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                Log.e("mgg","加密开始");
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                Log.e("mgg","加密zhong");
                try {
                    ConvertNormalToSQLCipheredDB(MainApp.application, AppDatabase.NAME+".db",DATABASE_NAME);
                    return  true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

            }

            @Override
            protected void onPostExecute(Boolean cursor) {
                if(true) {
                    Log.e("mgg", "加密完成");
                }else{
                    Log.e("mgg", "加密Failed");
                }
            }
        }.execute();
    }

}
