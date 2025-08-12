package com.eselman.cities.challenge.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.eselman.cities.challenge.db.dao.CitiesDao
import com.eselman.cities.challenge.network.services.CitiesApiService
import com.eselman.cities.challenge.repositories.CitiesRepositoriesImpl
import com.eselman.cities.challenge.repositories.CitiesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelsModule {
    @Provides
    fun providesCitiesRepository(
        citiesApiService: CitiesApiService,
        citiesDao: CitiesDao,
        dataStore: DataStore<Preferences>
    ): CitiesRepository = CitiesRepositoriesImpl(
        citiesApiService,
        citiesDao,
        dataStore
    )
}
