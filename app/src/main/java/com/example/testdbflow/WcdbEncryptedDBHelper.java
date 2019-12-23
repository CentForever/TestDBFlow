package com.example.testdbflow;

import android.content.Context;
import android.os.AsyncTask;


import com.tencent.mars.xlog.Log;
import com.tencent.wcdb.DatabaseErrorHandler;
import com.tencent.wcdb.DatabaseUtils;
import com.tencent.wcdb.database.SQLiteChangeListener;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;
import com.tencent.wcdb.repair.RepairKit;

import java.io.File;


public class WcdbEncryptedDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "WcdbEncryptedDBHelper";

    public static final String DATABASE_NAME = "encrypted.db";
    public static final String OLD_DATABASE_NAME = "plain-text";
    public static final int DATABASE_VERSION = 2;

    private Context mContext;
    private String mPassphrase;


    // The test database is taken from SQLCipher test-suit.
    //
    // To be compatible with databases created by the official SQLCipher
    // library, a SQLiteCipherSpec must be specified with page size of
    // 1024 bytes.
    static final SQLiteCipherSpec CIPHER_SPEC = new SQLiteCipherSpec()
            .setPageSize(1024);


    // We don't want corrupted databases get deleted or renamed on this sample,
    // so use an empty DatabaseErrorHandler.
    static final DatabaseErrorHandler ERROR_HANDLER = new DatabaseErrorHandler() {
        @Override
        public void onCorruption(SQLiteDatabase dbObj) {
            // Do nothing
        }
    };
    public WcdbEncryptedDBHelper(Context context, String passphrase) {

        // Call "encrypted" version of the superclass constructor.
        super(context, DATABASE_NAME, passphrase.getBytes(), CIPHER_SPEC, null, DATABASE_VERSION,
                ERROR_HANDLER);
        // Save context object for later use.
        mContext = context;
        mPassphrase = passphrase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Check whether old plain-text database exists, if so, export it
        // to the new, encrypted one.
        File oldDbFile = mContext.getDatabasePath(OLD_DATABASE_NAME+".db");
        if (oldDbFile.exists()) {

            Log.i(TAG, "Migrating plain-text database to encrypted one.");

            // SQLiteOpenHelper begins a transaction before calling onCreate().
            // We have to end the transaction before we can attach a new database.
            db.endTransaction();

            // Attach old database to the newly created, encrypted database.
            String sql = String.format("ATTACH DATABASE %s AS old KEY '';",
                    DatabaseUtils.sqlEscapeString(oldDbFile.getPath()));
            db.execSQL(sql);

            // Export old database.
            db.beginTransaction();
            DatabaseUtils.stringForQuery(db, "SELECT sqlcipher_export('main', 'old');", null);
            db.setTransactionSuccessful();
            db.endTransaction();

            // Get old database version for later upgrading.
            int oldVersion = (int) DatabaseUtils.longForQuery(db, "PRAGMA old.user_version;", null);

            // Detach old database and enter a new transaction.
            db.execSQL("DETACH DATABASE old;");

            // Old database can be deleted now.
            //oldDbFile.delete();

            // Before further actions, restore the transaction.
            db.beginTransaction();

            // Check if we need to upgrade the schema.
            if (oldVersion > DATABASE_VERSION) {
                onDowngrade(db, oldVersion, DATABASE_VERSION);
            } else if (oldVersion < DATABASE_VERSION) {
                onUpgrade(db, oldVersion, DATABASE_VERSION);
            }
        } else {
            Log.i(TAG, "Creating new encrypted database.");

            // Do the real initialization if the old database is absent.
            db.execSQL("CREATE TABLE message (content TEXT, "
                    + "sender TEXT);");
        }

        // OPTIONAL: backup master info for corruption recovery.
        RepairKit.MasterInfo.save(db, db.getPath() + "-mbak", mPassphrase.getBytes());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, String.format("Upgrading database from version %d to version %d.",
                oldVersion, newVersion));

        // Add new column to message table on database upgrade.
        db.execSQL("ALTER TABLE message ADD COLUMN sender TEXT;");

        // OPTIONAL: backup master info for corruption recovery.
        RepairKit.MasterInfo.save(db, db.getPath() + "-mbak", mPassphrase.getBytes());
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setAsyncCheckpointEnabled(true);
        db.setChangeListener(new SQLiteChangeListener() {

            private StringBuilder mSB = new StringBuilder();
            private void printIds(String prefix, long[] ids) {
                mSB.append(prefix).append(": ");
                for (long id : ids) {
                    mSB.append(id).append(", ");
                }
                Log.i(TAG, mSB.toString());
                mSB.setLength(0);
            }

            @Override
            public void onChange(SQLiteDatabase db, String dbName, String table,
                                 long[] insertIds, long[] updateIds, long[] deleteIds) {
                Log.i(TAG, "onChange called: dbName = " + dbName + ", table = " + table);
                printIds("INSERT", insertIds);
                printIds("UPDATE", updateIds);
                printIds("DELETE", deleteIds);
            }
        }, true);
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
                 SQLiteDatabase mDB = null;
                 SQLiteOpenHelper mDBHelper=null;
                 int mDBVersion;
                if (mDBHelper != null && mDB != null && mDB.isOpen()) {
                    mDBHelper.close();
                    mDBHelper = null;
                    mDB = null;
                }
                String passphrase = "mgg";
                mDBHelper = new WcdbEncryptedDBHelper(MainApp.application, passphrase);
                mDBHelper.setWriteAheadLoggingEnabled(true);
                mDB = mDBHelper.getWritableDatabase();
                mDBVersion = mDB.getVersion();
                mDB.rawQuery("select count(1) from sqlite_master where type in('table','view','trigger');",
                        null);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean cursor) {
                Log.e("mgg","加密完成");
            }
        }.execute();
    }
}