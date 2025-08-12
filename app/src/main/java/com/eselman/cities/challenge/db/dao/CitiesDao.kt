package com.eselman.cities.challenge.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eselman.cities.challenge.db.entities.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiesDao {
    @Query("SELECT * FROM cities ORDER BY name,country")
    fun getCities(): Flow<List<CityEntity>>

    @Query("SELECT * FROM cities WHERE id=:cityId")
    fun getCityById(cityId: Long): Flow<CityEntity?>

    @Query("SELECT id FROM cities WHERE isFavorite = 1")
    fun getFavoriteCitiesIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Query("DELETE FROM cities")
    suspend fun clearAllCities()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCity(cityEntity: CityEntity)
}
