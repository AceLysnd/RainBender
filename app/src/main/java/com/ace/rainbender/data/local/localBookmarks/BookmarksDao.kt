package com.ace.rainbender.data.local.localBookmarks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ace.rainbender.data.local.user.AccountEntity
import com.ace.rainbender.data.model.geocoding.Result

@Dao
interface BookmarksDao {

    @Insert
    fun inserBookmarks(bookmarks: BookmarksEntity): Long

    @Update
    fun updateAccount(account: BookmarksEntity): Int

    @Query("SELECT * FROM BOOKMARKS")
    fun getAllBookmarks() : BookmarksEntity?
}