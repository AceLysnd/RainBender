package com.ace.rainbender.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AccountEntity::class], version = 4, exportSchema = false)
abstract class AccountDatabase : RoomDatabase() {

    abstract val accountDao : AccountDao
}