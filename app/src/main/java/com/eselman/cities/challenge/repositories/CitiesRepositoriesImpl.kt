package com.eselman.cities.challenge.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eselman.cities.challenge.db.dao.CitiesDao
import com.eselman.cities.challenge.db.entities.toCity
import com.eselman.cities.challenge.model.CitiesResult
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.model.toCityEntity
import com.eselman.cities.challenge.network.responses.CityResponse
import com.eselman.cities.challenge.network.responses.toCityEntity
import com.eselman.cities.challenge.network.services.CitiesApiService
import com.eselman.cities.challenge.utils.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CitiesRepositoriesImpl @Inject constructor(
    private val citiesApiService: CitiesApiService,
    private val citiesDao: CitiesDao,
    private val dataStore: DataStore<Preferences>
) : CitiesRepository {

    companion object {
        val SEARCH_CITIES_TEXT = stringPreferencesKey(AppConstants.SEARCH_CITIES_TEXT)
        val SEARCH_ONLY_FAVORITE_CITIES =
            booleanPreferencesKey(AppConstants.SEARCH_ONLY_FAVORITE_CITIES)
    }

    override val searchTextPreferences: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SEARCH_CITIES_TEXT] ?: ""
        }

    override val searchOnlyFavorites: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SEARCH_ONLY_FAVORITE_CITIES] ?: false
        }

    override suspend fun saveSearchText(searchText: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_CITIES_TEXT] = searchText
        }
    }

    override suspend fun saveOnlyFavoriteCities(onlyFavoriteCities: Boolean) {
        dataStore.edit { preferences ->
            preferences[SEARCH_ONLY_FAVORITE_CITIES] = onlyFavoriteCities
        }
    }

    override suspend fun getCities(): Flow<CitiesResult?> {
        var citiesResult: CitiesResult?
        try {
            val cities = citiesDao.getCities()
                .map { citiesDb ->
                    citiesDb.map { cityEntity -> cityEntity.toCity() }
                }.first()

            citiesResult = if (cities.isNotEmpty()) {
                CitiesResult.CitiesResultSuccess(cities = cities)
            } else {
                refreshCities().first()
            }
        } catch (e: Exception) {
            citiesResult = CitiesResult.CitiesResultError(e.localizedMessage)
        }
        return flowOf(citiesResult)
    }

    override suspend fun refreshCities(): Flow<CitiesResult> {
        try {
            val citiesList = getCitiesFromApi()
            if (citiesList.isNullOrEmpty()) {
                return flowOf(CitiesResult.CitiesResultError("Cities were not retrieved from API"))
            } else {
                val favoriteCitiesIds =
                    citiesDao.getFavoriteCitiesIds().firstOrNull() ?: emptyList()
                citiesDao.clearAllCities()
                citiesList.map { cityResponse ->
                    val isFavoriteCity = cityResponse.id in favoriteCitiesIds
                    citiesDao.insertCity(
                        cityResponse.toCityEntity().copy(isFavorite = isFavoriteCity)
                    )
                }
                val cities = citiesDao.getCities()
                    .map { citiesDb -> citiesDb.map { cityEntity -> cityEntity.toCity() } }.first()
                return flowOf(CitiesResult.CitiesResultSuccess(cities = cities))
            }
        } catch (e: Exception) {
            return flowOf(CitiesResult.CitiesResultError(e.localizedMessage))
        }
    }

    override suspend fun filterCities(
        query: String,
        onlyFavoriteCities: Boolean
    ): Flow<List<City>> {
        try {
            val cities = citiesDao.getCities()
                .map { citiesDb -> citiesDb.map { cityEntity -> cityEntity.toCity() } }.first()

            val filteredCities = if (onlyFavoriteCities) {
                cities.filter {
                    it.isFavorite && it.name.startsWith(query, true)
                }
            } else {
                cities.filter {
                    it.name.startsWith(query, true)
                }
            }
            return flowOf(filteredCities)
        } catch (e: Exception) {
            // TODO: Handle Error
            return flowOf(emptyList())
        }
    }

    override suspend fun updateCity(city: City) = citiesDao.updateCity(city.toCityEntity())

    override suspend fun getCityById(cityId: Long): Flow<City?> {
        val cityEntity = citiesDao.getCityById(cityId).firstOrNull()
        return if (cityEntity != null) {
            flowOf(cityEntity.toCity())
        } else {
            flowOf(null)
        }
    }

    private suspend fun getCitiesFromApi(): List<CityResponse>? = try {
        citiesApiService.getCities()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
