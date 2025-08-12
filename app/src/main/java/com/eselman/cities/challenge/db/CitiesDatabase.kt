package com.eselman.cities.challenge.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eselman.cities.challenge.db.dao.CitiesDao
import com.eselman.cities.challenge.db.entities.CityEntity

@Database(
    entities = [
        CityEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class CitiesDatabase : RoomDatabase() {
    abstract fun getCitiesDao(): CitiesDao
}
