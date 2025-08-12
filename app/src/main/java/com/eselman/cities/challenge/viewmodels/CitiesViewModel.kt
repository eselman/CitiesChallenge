package com.eselman.cities.challenge.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eselman.cities.challenge.model.CitiesResult
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.repositories.CitiesRepository
import com.eselman.cities.challenge.viewmodels.CitiesUIState.Error
import com.eselman.cities.challenge.viewmodels.CitiesUIState.Loading
import com.eselman.cities.challenge.viewmodels.CitiesUIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository
) : ViewModel() {
    val citiesUIState = MutableStateFlow<CitiesUIState>(Loading)
    val selectedCity = MutableStateFlow<City?>(null)
    val searchText = MutableStateFlow("")
    val onlyFavoriteCities = MutableStateFlow(false)
    val isFiltering = MutableStateFlow(false)

    init {
        getCities()
    }

    fun getCities() {
        viewModelScope.launch {
            citiesUIState.value = Loading
            citiesRepository.getCities()
                .flowOn(Dispatchers.IO)
                .collect {
                    when (it) {
                        is CitiesResult.CitiesResultSuccess -> {
                            citiesUIState.value = Success(cities = it.cities ?: emptyList())
                            selectedCity.value = it.cities?.firstOrNull()
                        }

                        is CitiesResult.CitiesResultError -> {
                            citiesUIState.value = Error(it.errorMessage)
                        }

                        else -> citiesUIState.value = Error("Unknown Error")
                    }
                }
        }
    }

    fun filterCities(
        query: String,
        onlyFavoriteCities: Boolean
    ) {
        viewModelScope.launch {
            isFiltering.value = true
            citiesRepository.filterCities(
                query,
                onlyFavoriteCities
            )
                .flowOn(Dispatchers.IO)
                .collect {
                    citiesUIState.value = Success(cities = it)
                    selectedCity.value = it.firstOrNull()
                    isFiltering.value = false
                }
        }
    }

    fun updateSelectedCity(city: City) {
        selectedCity.value = city
    }

    fun updateCityIsFavorite(city: City) {
        viewModelScope.launch {
            citiesRepository.updateCity(city)
            val updatedCities = mutableListOf<City>()
            val cities = when (citiesUIState.value) {
                is Success -> (citiesUIState.value as Success).cities
                else -> null
            }
            if (cities?.isNotEmpty() == true) {
                updatedCities.addAll(cities)
                val updatedCityIndex = updatedCities.indexOfFirst { it.id == city.id }
                updatedCities.removeAt(updatedCityIndex)
                val onlyFavorites = onlyFavoriteCities.value
                if (!onlyFavorites || city.isFavorite) {
                    updatedCities.add(updatedCityIndex, city)
                    if (selectedCity.value?.id == city.id) {
                        selectedCity.value = city
                    }
                }
                citiesUIState.value = Success(updatedCities)

            }
        }
    }

    fun updateSearchText(newSearchText: String) {
        viewModelScope.launch {
            searchText.value = newSearchText
            citiesRepository.saveSearchText(newSearchText)
        }
    }

    fun updateOnlyFavoriteCities(newOnlyFavoriteCities: Boolean) {
        viewModelScope.launch {
            onlyFavoriteCities.value = newOnlyFavoriteCities
            citiesRepository.saveOnlyFavoriteCities(newOnlyFavoriteCities)
        }
    }
}

sealed class CitiesUIState {
    data object Loading : CitiesUIState()
    data class Success(val cities: List<City>) : CitiesUIState()
    data class Error(val errorMessage: String?) : CitiesUIState()
}
