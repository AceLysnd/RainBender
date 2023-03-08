package com.ace.rainbender.data.local.user

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [AccountEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AccountDatabase : RoomDatabase() {

    abstract val accountDao : AccountDao
}

val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE account_information ADD COLUMN profilePicture TEXT")
    }
}

val MIGRATION_2_3 = object : Migration(2,3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE account_information ADD COLUMN bookmark TEXT")
    }
}