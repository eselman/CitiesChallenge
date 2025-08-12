package com.eselman.cities.challenge.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eselman.cities.challenge.model.City

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val country: String,
    val name: String,
    val lon: Double,
    val lat: Double,
    val isFavorite: Boolean = false
)

fun CityEntity.toCity() = City(
    id = id,
    country = country,
    name = name,
    lon = lon,
    lat = lat,
    isFavorite = isFavorite
)
