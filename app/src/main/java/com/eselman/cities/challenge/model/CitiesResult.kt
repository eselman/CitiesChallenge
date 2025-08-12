package com.eselman.cities.challenge.model

sealed class CitiesResult {
    data class CitiesResultSuccess(val cities: List<City>?) : CitiesResult()
    data class CitiesResultError(val errorMessage: String?) : CitiesResult()
}
