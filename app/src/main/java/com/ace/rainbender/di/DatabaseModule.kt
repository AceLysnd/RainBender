package com.ace.rainbender.di

import android.content.Context
import androidx.room.Room
import com.ace.rainbender.data.local.user.AccountDao
import com.ace.rainbender.data.local.user.AccountDatabase
import com.ace.rainbender.data.local.user.AccountEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideDao(appDatabase: AccountDatabase): AccountDao {
        return appDatabase.accountDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context):
            AccountDatabase {
        return Room.databaseBuilder(
            appContext,
            AccountDatabase::class.java,
            "app_database"
        ).build()
    }
}