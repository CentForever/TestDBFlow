package com.example.testdbflow

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
object AppDatabase {
    const val NAME = WcdbEncryptedDBHelper.OLD_DATABASE_NAME
    const val VERSION = WcdbEncryptedDBHelper.DATABASE_VERSION
}

@Table(name = "Users", database = AppDatabase::class)
data class User(@PrimaryKey var id: Int = 0, @Column var name: String? = null)