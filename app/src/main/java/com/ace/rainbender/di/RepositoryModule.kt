package com.ace.rainbender.di

import android.content.Context
import com.ace.rainbender.data.local.user.AccountDataSource
import com.ace.rainbender.data.model.AccountDataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

//    @ViewModelScoped
//    @Provides
//    fun provideRepository(apiHelper: ApiHelper) = WeatherRepository(apiHelper)

    @ViewModelScoped
    @Provides
    fun provideDataSource(accountDataSource: AccountDataSource, prefs: AccountDataStoreManager) =
        LocalRepository(accountDataSource, prefs)


    @ViewModelScoped
    @Provides
    fun provideContext(@ApplicationContext context: Context) = AccountDataStoreManager(context)
}