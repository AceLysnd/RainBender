package com.ace.rainbender.data.local.user

import android.graphics.Picture
import com.ace.rainbender.data.model.geocoding.Result
import com.ace.rainbender.ui.view.BookmarkFragment
import javax.inject.Inject

class AccountDataSource @Inject constructor(private val accountDao: AccountDao) {

    suspend fun getAccountById(id: Long): AccountEntity? {
        return accountDao.getAccountById(id)
    }

    suspend fun registerAccount(account: AccountEntity): Long{
        return accountDao.registerAccount(account)
    }

    suspend fun updateAccount(account: AccountEntity): Int {
        return accountDao.updateAccount(account)
    }

    suspend fun getUser(username: String) : AccountEntity {
        return accountDao.getUser(username)
    }

    suspend fun updateProfilePicture(id: Long, profilePicture: String) : Int {
        return accountDao.updateProfilePic(id, profilePicture)
    }

    suspend fun updateBookmark(id: Long, bookmark: MutableList<Result>) : Int {
        return accountDao.updateBookmark(id, bookmark)
    }
}