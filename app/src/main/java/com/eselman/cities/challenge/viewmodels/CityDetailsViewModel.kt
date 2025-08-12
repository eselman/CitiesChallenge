package com.eselman.cities.challenge.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.repositories.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailsViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val city = MutableStateFlow<City?>(null)

    init {
        val cityId = savedStateHandle.get<Long>("cityId") ?: 0
        getCityById(cityId)
    }


    fun getCityById(cityId: Long) {
        viewModelScope.launch {
            citiesRepository.getCityById(cityId)
                .flowOn(Dispatchers.IO)
                .collect {
                    city.value = it
                }
        }
    }
}
