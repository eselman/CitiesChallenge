package com.eselman.cities.challenge.network.services

import com.eselman.cities.challenge.network.responses.CityResponse
import retrofit2.http.GET

interface CitiesApiService {
    @GET("cities.json")
    suspend fun getCities(): List<CityResponse>
}
