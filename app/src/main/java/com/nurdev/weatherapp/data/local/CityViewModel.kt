package com.nurdev.weatherapp.data.local

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {
    val repository: CityRepo
    val allCities: LiveData<List<City>>

    init {
        val dao = CityDB.getDatabase(application).getCityDAO()
        repository = CityRepo(dao)
        allCities = repository.allCities
    }

    fun addCity(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCity(city)
    }

    fun removeCity(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCity(city)
    }

    fun clearFavorites() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllCities()
    }

    fun removeByName(city: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByName(city)
    }

}