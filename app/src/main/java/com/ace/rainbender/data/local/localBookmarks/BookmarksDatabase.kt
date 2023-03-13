package com.ace.rainbender.data.local.localBookmarks

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [BookmarksEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BookmarksDatabase : RoomDatabase() {

    abstract val bookmarksDao : BookmarksDao
}
