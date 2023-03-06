package com.ace.rainbender.data.model

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationDataStoreManager @Inject constructor(@ActivityContext private val context: Context) {

    suspend fun setLocation(latitude: Double, longitude: Double, location: String) {
        context.locationDataStore.edit { preferences ->
            preferences[LOCATION_LONGITUDE] = longitude
            preferences[LOCATION_LATITUDE] = latitude
            preferences[LOCATION_NAME] = location
        }
    }

    fun getLocation(): Flow<Location> {
        return context.locationDataStore.data.map { preferences ->
            Location(
                (preferences[LOCATION_LATITUDE] ?: 0) as Double,
                (preferences[LOCATION_LONGITUDE] ?: 0) as Double,
                preferences[LOCATION_NAME] ?: "",
            )
        }
    }

    companion object {
        private const val DATASTORE_NAME = "location_prefs"

        private val LOCATION_LATITUDE = doublePreferencesKey("location_latitude")

        private val LOCATION_LONGITUDE = doublePreferencesKey("location_longitude")

        private val LOCATION_NAME = stringPreferencesKey("location_name")

        private val Context.locationDataStore by preferencesDataStore(name = DATASTORE_NAME)
    }
}