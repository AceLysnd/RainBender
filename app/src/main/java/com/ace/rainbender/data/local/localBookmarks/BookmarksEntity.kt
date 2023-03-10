package com.ace.rainbender.data.local.localBookmarks

import androidx.room.*
import com.ace.rainbender.data.model.geocoding.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "bookmarks")
data class BookmarksEntity(
    @PrimaryKey(autoGenerate = true)
    var accountId: Long = 0,
    @ColumnInfo(name = "bookmark")
    var bookmark: MutableList<Result>?
)

class Converters {
    @TypeConverter
    fun listToJson(value: List<Result>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String?): List<Result>? {
        return if (value != null) {
            Gson().fromJson(value, object : TypeToken<List<Result>?>() {}.type)
        } else null
    }
}