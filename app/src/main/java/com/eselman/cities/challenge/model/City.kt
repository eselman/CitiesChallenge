package com.eselman.cities.challenge.model

import android.os.Build
import com.eselman.cities.challenge.db.entities.CityEntity
import java.util.Locale

data class City(
    val id: Long,
    val country: String,
    val name: String,
    val lon: Double,
    val lat: Double,
    val isFavorite: Boolean
)

fun City.toCityEntity() = CityEntity(
    id = id,
    country = country,
    name = name,
    lon = lon,
    lat = lat,
    isFavorite = isFavorite
)

fun City.getDisplayCountry() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
    Locale.of("", country).displayCountry
} else {
    Locale("", country).displayCountry
}
