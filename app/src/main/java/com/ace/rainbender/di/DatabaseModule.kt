package com.ace.rainbender.di

import android.content.Context
import androidx.room.Room
import com.ace.rainbender.data.local.localweather.daily.DailyWeatherDao
import com.ace.rainbender.data.local.localweather.daily.DailyWeatherDatabase
import com.ace.rainbender.data.local.localweather.hourly.HourlyWeatherDao
import com.ace.rainbender.data.local.localweather.hourly.HourlyWeatherDatabase
import com.ace.rainbender.data.local.user.AccountDao
import com.ace.rainbender.data.local.user.AccountDatabase
import com.ace.rainbender.data.local.user.MIGRATION_1_2
import com.ace.rainbender.data.local.user.MIGRATION_2_3
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
    fun provideHourlyDao(hourlyWeatherDatabase: HourlyWeatherDatabase): HourlyWeatherDao {
        return hourlyWeatherDatabase.hourlyWeatherDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context):
            AccountDatabase {
        return Room.databaseBuilder(
            appContext,
            AccountDatabase::class.java,
            "app_database"
        )
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideDailyWeatherDatabase(@ApplicationContext appContext: Context):
            DailyWeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            DailyWeatherDatabase::class.java,
            "daily_weather_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHourlyWeatherDatabase(@ApplicationContext appContext: Context):
            HourlyWeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            HourlyWeatherDatabase::class.java,
            "hourly_weather_database"
        ).build()
    }
}