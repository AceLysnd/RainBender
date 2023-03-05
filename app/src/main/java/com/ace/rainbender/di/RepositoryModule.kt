package com.ace.rainbender.di

import android.content.Context
import com.ace.rainbender.data.local.localweather.DailyWeatherDataSource
import com.ace.rainbender.data.local.user.AccountDataSource
import com.ace.rainbender.data.model.AccountDataStoreManager
import com.ace.rainbender.data.model.Location
import com.ace.rainbender.data.model.LocationDataStoreManager
import com.ace.rainbender.data.model.Prefs
import com.ace.rainbender.data.services.WeatherApiHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideWeatherRepository(weatherApiHelper: WeatherApiHelper) = WeatherRepository(weatherApiHelper)

    @ViewModelScoped
    @Provides
    fun provideDataSource(accountDataSource: AccountDataSource,
                          prefs: AccountDataStoreManager,
                          weatherDataSource: DailyWeatherDataSource,
                          locationPrefs: LocationDataStoreManager) =
        LocalRepository(accountDataSource, prefs, weatherDataSource, locationPrefs)


    @ViewModelScoped
    @Provides
    fun provideContext(@ApplicationContext context: Context) = AccountDataStoreManager(context)

    @ViewModelScoped
    @Provides
    fun provideContext2(@ApplicationContext context: Context) = LocationDataStoreManager(context)
}