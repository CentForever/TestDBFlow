package com.example.testdbflow

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.Select
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonNormalInitDb.setOnClickListener {
            FlowManager.init(FlowConfig.Builder(this)
                 .openDatabasesOnInit(true)
                .addDatabaseConfig(DatabaseConfig.builder(AppDatabase::class.java)
                    .databaseName(AppDatabase.NAME).build())
                .build())
        }
        buttonNormalInsetData.setOnClickListener {
            val user = User()
            user.id = System.currentTimeMillis().toInt()
            user.name = System.currentTimeMillis().toString()
            user.save()
            Toast.makeText(this, "data created", Toast.LENGTH_SHORT).show()
        }

        buttonNormalReadData.setOnClickListener {
            val datas = Select().from(User::class.java).list
            resultData.text= "result:\n$datas"
        }
        buttonWcdbEncryptedDb.setOnClickListener {
            FlowManager.getDatabase(AppDatabase::class.java).close()
            WcdbEncryptedDBHelper.test()
        }

        buttonOpenWcdbEncryptedDb.setOnClickListener {
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
            resultData.text= "Encrypted result:\n$datas"
        }

        buttonNetSqlcipherEncryptedDb.setOnClickListener {
            FlowManager.getDatabase(AppDatabase::class.java).close()
            NetSqlcipherHelper.test()
        }

        buttonOpenNetSqlcipherEncryptedDb.setOnClickListener {
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
            resultData.text= "Encrypted result:\n$datas"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
