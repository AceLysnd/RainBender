package com.ace.rainbender.data.local.user

import androidx.room.*
import com.ace.rainbender.data.model.geocoding.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "account_information")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    var accountId: Long = 0,

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "profilePicture")
    var profilePicture: String?,

    @ColumnInfo(name = "bookmark")
    var bookmark: List<Result?>?
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