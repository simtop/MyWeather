package com.simtop.myweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.simtop.myweather.data.db.entity.TodaysWeatherEntry
import com.simtop.myweather.data.db.entity.WeatherLocation

@Database(
    entities = [TodaysWeatherEntry::class, WeatherLocation::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): TodaysWeatherDao
    abstract fun weatherLocationDao() : WeatherLocationDao

    companion object {
        @Volatile private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherDatabase::class.java, "weather_database.db")
                .build()
    }
}