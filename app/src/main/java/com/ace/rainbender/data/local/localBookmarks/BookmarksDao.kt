package com.ace.rainbender.data.local.localBookmarks

import androidx.room.*
import com.ace.rainbender.data.local.user.AccountEntity
import com.ace.rainbender.data.model.geocoding.Result

@Dao
interface BookmarksDao {

    @Insert
    fun inserBookmarks(bookmarks: BookmarksEntity): Long

    @Update
    fun updateBookmark(bookmarks: BookmarksEntity): Int

    @Delete
    fun deleteAllBookmarks(bookmarks: BookmarksEntity): Int

    @Query("SELECT * FROM BOOKMARKS")
    fun getAllBookmarks() : BookmarksEntity?

    @Query("SELECT * FROM BOOKMARKS WHERE accountId == :accountId")
    fun getBookmarksById(accountId : Long) : BookmarksEntity?
}