package com.ace.rainbender.di

import android.content.Context
import androidx.room.Room
import com.ace.rainbender.data.local.localweather.DailyWeatherDao
import com.ace.rainbender.data.local.localweather.DailyWeatherDatabase
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
    fun provideDailyDao(dailyWeatherDatabase: DailyWeatherDatabase): DailyWeatherDao {
        return dailyWeatherDatabase.dailyWeatherDao
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

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext appContext: Context):
            DailyWeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            DailyWeatherDatabase::class.java,
            "daily_weather_database"
        ).build()
    }
}