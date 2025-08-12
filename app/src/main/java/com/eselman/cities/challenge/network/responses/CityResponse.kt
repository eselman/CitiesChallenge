package com.eselman.cities.challenge.network.responses

import com.eselman.cities.challenge.db.entities.CityEntity
import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("_id")
    val id: Long,
    val country: String,
    val name: String,
    val coord: CoordResponse
)

fun CityResponse.toCityEntity() = CityEntity(
    id = id,
    country = country,
    name = name,
    lat = coord.lat,
    lon = coord.lon
)
