package com.ace.rainbender.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [AccountEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AccountDatabase : RoomDatabase() {

    abstract val accountDao : AccountDao
}