package com.ace.rainbender.data.local.localBookmarks

import com.ace.rainbender.data.model.geocoding.Result
import javax.inject.Inject

class BookmarksDataSource @Inject constructor(private val bookmarksDao: BookmarksDao) {

    fun insertBookmarks(bookmarksEntity: BookmarksEntity):Long {
        return bookmarksDao.inserBookmarks(bookmarksEntity)
    }

    fun updateBookmarks(bookmarksEntity: BookmarksEntity):Int {
        return bookmarksDao.updateAccount(bookmarksEntity)
    }

    fun getBookmarks(): BookmarksEntity? {
        return bookmarksDao.getAllBookmarks()
    }
}