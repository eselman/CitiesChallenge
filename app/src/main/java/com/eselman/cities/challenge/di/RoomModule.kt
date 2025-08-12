package com.eselman.cities.challenge.di

import android.content.Context
import androidx.room.Room
import com.eselman.cities.challenge.db.CitiesDatabase
import com.eselman.cities.challenge.db.dao.CitiesDao
import com.eselman.cities.challenge.utils.AppConstants.CITIES_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Singleton
    @Provides
    fun providesCitiesDatabase(@ApplicationContext context: Context): CitiesDatabase =
        Room.databaseBuilder(
            context,
           CitiesDatabase::class.java,
           CITIES_DB_NAME
        ).build()

    @Singleton
    @Provides
    fun providesCitiesDao(citiesDatabase: CitiesDatabase): CitiesDao =
        citiesDatabase.getCitiesDao()
}
