package com.nurdev.weatherapp.data.local

import androidx.lifecycle.LiveData

class CityRepo(
    private val citiesDAO: CitiesDAO,
) {
    val allCities: LiveData<List<City>> = citiesDAO.getAllCities()

    suspend fun insertCity(city: City) = citiesDAO.insert(city)

    suspend fun deleteCity(city: City) = citiesDAO.delete(city)

    suspend fun deleteByName(city: String) = citiesDAO.deleteByName(city)

    fun deleteAllCities() = citiesDAO.reset()
}