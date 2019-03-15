package com.simtop.myweather.data.db

import android.content.Context
import androidx.room.*
import com.simtop.myweather.data.db.entity.TodaysWeatherEntry
import com.simtop.myweather.data.db.entity.WeatherLocation

@Database(
    entities = [TodaysWeatherEntry::class, WeatherLocation::class],
    version = 1
)
@TypeConverters(LocalDateConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): TodaysWeatherDao
    abstract fun futureWeaatherDao() : FutureWeatherDao
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