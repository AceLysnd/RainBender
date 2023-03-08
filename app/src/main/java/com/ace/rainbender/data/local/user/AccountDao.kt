package com.ace.rainbender.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ace.rainbender.data.model.geocoding.Result

@Dao
interface AccountDao {

    @Insert
    suspend fun registerAccount(account: AccountEntity): Long

    @Update
    suspend fun updateAccount(account: AccountEntity): Int

    @Query("SELECT * FROM account_information WHERE username = :username")
    suspend fun getUser(username: String) : AccountEntity

    @Query("SELECT * FROM ACCOUNT_INFORMATION WHERE accountId == :id LIMIT 1")
    suspend fun getAccountById(id : Long) : AccountEntity?

    @Query("SELECT * FROM ACCOUNT_INFORMATION")
    suspend fun getAllAccount() : List<AccountEntity>

    @Query("UPDATE ACCOUNT_INFORMATION SET profilePicture = :profilePicture WHERE accountId = :accountId " )
    suspend fun updateProfilePic(accountId: Long, profilePicture: String) : Int

    @Query("UPDATE ACCOUNT_INFORMATION SET bookmark = :bookmark WHERE accountId = :accountId " )
    suspend fun updateBookmark(accountId: Long, bookmark: List<Result>) : Int
}