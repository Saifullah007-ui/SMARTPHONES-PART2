package com.nurdev.weatherapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CitiesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: City)

    @Delete
    suspend fun delete(city: City)

    @Query("SELECT * FROM cities_table")
    fun getAllCities(): LiveData<List<City>>

    @Query("DELETE FROM cities_table")
    fun reset()

    @Query("DELETE FROM cities_table WHERE city_name = :name")
    suspend fun deleteByName(name: String)
}