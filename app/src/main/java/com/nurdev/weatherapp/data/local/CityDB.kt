package com.nurdev.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(
    entities = [City::class],
    version = 1
)
abstract class CityDB : RoomDatabase() {

    abstract fun getCityDAO(): CitiesDAO

    companion object {
        @Volatile
        private var INSTANCE: CityDB? = null

        fun getDatabase(context: Context): CityDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDB::class.java,
                    "cities_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}