package com.example.testdbflow

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dianping.logan.Logan
import com.example.testdbflow.db.NetSqlcipherHelper
import com.example.testdbflow.db.SQLCipherHelperImpl
import com.example.testdbflow.db.WcdbEncryptedDBHelper
import com.example.testdbflow.xlog.ZipLogFile
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.sql.language.Select
import com.tencent.mars.xlog.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logan.w("APP START", 2)
        Log.d("APP START","MainActivity")
        buttonNormalInitDb.setOnClickListener {
            initDefaultDb()
        }
        buttonNormalInsetData.setOnClickListener {
            try {
                //closeDb()
                val user = User()
                user.id = UUID.randomUUID().toString()
                user.name = System.currentTimeMillis().toString()
                user.save()
                Toast.makeText(this, "data created", Toast.LENGTH_SHORT).show()
                resultData.text= "DB：${FlowManager.getDatabase(AppDatabase::class.java).databaseName} "
            }catch (e:Exception){
                resultData.text= "Exception result:\n${e.message}"
            }
        }

        buttonNormalReadData.setOnClickListener {
            try {
                //closeDb()
                val db=FlowManager.getDatabase(AppDatabase::class.java).writableDatabase
                val datas=SQLite.select().from(User::class.java).queryList(db)
                resultData.text= "${FlowManager.getDatabase(AppDatabase::class.java).databaseName} result:\n$datas"
            }catch (e:Exception){
                resultData.text= "Exception result:\n${e.message}"
            }
        }
        buttonWcdbEncryptedDb.setOnClickListener {
            try {
                closeDb()
                WcdbEncryptedDBHelper.test()
            }catch (e:Exception){
                resultData.text= "Exception result:\n${e.message}"
            }
        }

        buttonOpenWcdbEncryptedDb.setOnClickListener {
            try {
                closeDb()
                FlowManager.init(FlowConfig.Builder(this)
                    //.openDatabasesOnInit(true)
                    .addDatabaseConfig(DatabaseConfig.builder(AppDatabase::class.java)
                        .databaseName("encrypted")
                        .openHelper { databaseDefinition, helperListener ->
                            SQLCipherHelperImpl(
                                databaseDefinition,
                                helperListener
                            )
                        }
                        .build()
                    )
                    .build())
                val db=FlowManager.getDatabase(AppDatabase::class.java).writableDatabase
                val datas=SQLite.select().from(User::class.java).queryList(db)
                resultData.text= "WCDB Encrypted ${FlowManager.getDatabase(AppDatabase::class.java).databaseName} result:\n$datas"
            }catch (e:Exception){
                resultData.text= "Exception result:\n${e.message}"
            }
        }

        buttonNetSqlcipherEncryptedDb.setOnClickListener {
            try {
                closeDb()
                NetSqlcipherHelper.test()
            }catch (e:Exception){
                resultData.text= "Exception result:\n${e.message}"
            }
        }

        buttonOpenNetSqlcipherEncryptedDb.setOnClickListener {
            try {
                closeDb()
                FlowManager.init(FlowConfig.Builder(this)
                    //.openDatabasesOnInit(true)
                    .addDatabaseConfig(DatabaseConfig.builder(AppDatabase::class.java)
                        .databaseName("NetSqlcipher")
                        .openHelper { databaseDefinition, helperListener ->
                            SQLCipherHelperImpl(
                                databaseDefinition,
                                helperListener
                            )
                        }
                        .build()
                    )
                    .build())
                val db=FlowManager.getDatabase(AppDatabase::class.java).writableDatabase
                val datas=SQLite.select().from(User::class.java).queryList(db)
                resultData.text= "NetSqlcipher Encrypted ${FlowManager.getDatabase(AppDatabase::class.java).databaseName} result:\n$datas"
            }catch (e:Exception){
                resultData.text= "Exception result:\n${e.message}"
            }
        }

        buttonZipLogs.setOnClickListener {
            try {
                ZipLogFile.test()
            }catch (e:Exception){
                resultData.text= "Exception result:\n${e.message}"
            }
        }
    }

    private fun initDefaultDb() {
        FlowManager.init(
            FlowConfig.Builder(this)
                //.openDatabasesOnInit(true)
                .addDatabaseConfig(
                    DatabaseConfig.builder(AppDatabase::class.java)
                        .databaseName(WcdbEncryptedDBHelper.OLD_DATABASE_NAME).build()
                )
                .build()
        )
    }

    private fun closeDb(){
        try {
            FlowManager.close()
        }catch (e:Exception){
            e.printStackTrace()
            resultData.text= "Exception result:\n${e.message}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeDb()
    }
}
