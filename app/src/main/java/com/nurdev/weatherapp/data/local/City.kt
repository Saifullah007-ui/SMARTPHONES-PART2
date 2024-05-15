package com.nurdev.weatherapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities_table")
class City(
    @ColumnInfo(name = "city_name")
    var city: String = ""
) {
    @ColumnInfo(name = "city_id")
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}