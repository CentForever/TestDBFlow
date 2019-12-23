package com.example.testdbflow

import com.example.testdbflow.db.WcdbEncryptedDBHelper
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.util.*

@Database(version = AppDatabase.VERSION)
object AppDatabase {
    const val VERSION = WcdbEncryptedDBHelper.DATABASE_VERSION
}

@Table(name = "Users", database = AppDatabase::class)
data class User(@PrimaryKey var id: String = UUID.randomUUID().toString(), @Column var name: String? = null)