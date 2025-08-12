package com.eselman.cities.challenge.repositories

import com.eselman.cities.challenge.model.CitiesResult
import com.eselman.cities.challenge.model.City

import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    val searchTextPreferences: Flow<String>
    val searchOnlyFavorites: Flow<Boolean>
    suspend fun getCities(): Flow<CitiesResult?>
    suspend fun refreshCities(): Flow<CitiesResult>
    suspend fun filterCities(
        query: String,
        onlyFavoriteCities: Boolean
    ): Flow<List<City>>

    suspend fun updateCity(city: City)
    suspend fun saveSearchText(searchText: String)
    suspend fun saveOnlyFavoriteCities(onlyFavoriteCities: Boolean)
    suspend fun getCityById(cityId: Long): Flow<City?>
}
