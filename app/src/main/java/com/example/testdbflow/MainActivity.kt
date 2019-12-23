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
            FlowManager.init(FlowConfig.Builder(this)
                 .openDatabasesOnInit(true)
                .addDatabaseConfig(DatabaseConfig.builder(AppDatabase::class.java)
                    .databaseName(AppDatabase.NAME).build())
                .build())
        }
        buttonNormalInsetData.setOnClickListener {
            try {
                val user = User()
                user.id = UUID.randomUUID().toString()
                user.name = System.currentTimeMillis().toString()
                user.save()
                Toast.makeText(this, "data created", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                resultData.text= "Exception result:\n${e.message}"
            }
        }

        buttonNormalReadData.setOnClickListener {
            try {
                val datas = Select().from(User::class.java).list
                resultData.text = "result:\n$datas"
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
                FlowManager.init(FlowConfig.Builder(this)
                    .openDatabasesOnInit(true)
                    .addDatabaseConfig(DatabaseConfig.builder(AppDatabase::class.java)
                        .databaseName(WcdbEncryptedDBHelper.DATABASE_NAME)
                        .openHelper { databaseDefinition, helperListener ->
                            SQLCipherHelperImpl(
                                databaseDefinition,
                                helperListener
                            )
                        }
                        .build()
                    )
                    .build())

                val datas = Select().from(User::class.java).list
                resultData.text= "WCDB Encrypted result:\n$datas"
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
                FlowManager.init(FlowConfig.Builder(this)
                    .openDatabasesOnInit(true)
                    .addDatabaseConfig(DatabaseConfig.builder(AppDatabase::class.java)
                        .databaseName(NetSqlcipherHelper.DATABASE_NAME)
                        .openHelper { databaseDefinition, helperListener ->
                            SQLCipherHelperImpl(
                                databaseDefinition,
                                helperListener
                            )
                        }
                        .build()
                    )
                    .build())

                val datas = Select().from(User::class.java).list
                resultData.text= "NetSqlcipher Encrypted result:\n$datas"
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

    private fun closeDb(){
        try {
            FlowManager.getDatabase(AppDatabase::class.java).close()
        }catch (e:Exception){
            e.printStackTrace()
            resultData.text= "Exception result:\n${e.message}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
