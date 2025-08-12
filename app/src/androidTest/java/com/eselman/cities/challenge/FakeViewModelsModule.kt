package com.eselman.cities.challenge

import com.eselman.cities.challenge.di.ViewModelsModule
import com.eselman.cities.challenge.model.CitiesResult
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.repositories.CitiesRepository
import com.eselman.cities.challenge.utils.dummyCities
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ViewModelsModule::class]
)
@Module
object FakeViewModelsModule {
    @Singleton
    @Provides
    fun providesFakeCitiesRepository() = object: CitiesRepository {
        override val searchTextPreferences: Flow<String>
            get() = flowOf("")
        override val searchOnlyFavorites: Flow<Boolean>
            get() = flowOf(false)

        override suspend fun getCities(): Flow<CitiesResult?> =
            flowOf(
                CitiesResult.CitiesResultSuccess(
                    cities = dummyCities.sortedWith(compareBy({ it.name }, { it.country }))
                )
            )

        override suspend fun refreshCities(): Flow<CitiesResult> =
            flowOf(CitiesResult.CitiesResultSuccess(cities = dummyCities))

        override suspend fun filterCities(
            query: String,
            onlyFavoriteCities: Boolean
        ): Flow<List<City>> {
            val filteredCities = if (onlyFavoriteCities) {
                dummyCities.filter {
                    it.isFavorite && it.name.startsWith(query, true)
                }
            } else {
                dummyCities.filter {
                    it.name.startsWith(query, true)
                }
            }
            return flowOf(filteredCities)
        }

        override suspend fun updateCity(city: City) {}

        override suspend fun saveSearchText(searchText: String) {}

        override suspend fun saveOnlyFavoriteCities(onlyFavoriteCities: Boolean) {}

        override suspend fun getCityById(cityId: Long): Flow<City?> =
            flowOf(dummyCities.firstOrNull { it.id == cityId })
    }
}