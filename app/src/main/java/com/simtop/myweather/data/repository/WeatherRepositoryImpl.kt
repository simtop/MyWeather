package com.simtop.myweather.data.repository

import androidx.lifecycle.LiveData
import com.simtop.myweather.data.db.TodaysWeatherDao
import com.simtop.myweather.data.db.WeatherLocationDao
import com.simtop.myweather.data.db.entity.WeatherLocation
import com.simtop.myweather.data.db.unittype.today.UnitSpecificType
import com.simtop.myweather.data.network.WeatherNetworkDataSource
import com.simtop.myweather.data.network.response.TodaysWeatherResponse
import com.simtop.myweather.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

//We can use observeForever and GlobalScope, because the Repository doesn't have a lifecycle, it's destroyed with the app
class WeatherRepositoryImpl(
    private val todaysWeatherDao: TodaysWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {
    init {
        weatherNetworkDataSource.downloadedTodaysWeather.observeForever { newTodaysWeather ->
            persistFetchedTodaysWeather(newTodaysWeather)
        }

    }
    override suspend fun getTodaysWeather(type: Boolean): LiveData<out UnitSpecificType> {
        //withContext returns something
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext  if(type) todaysWeatherDao.getMetricWeather()
                                else todaysWeatherDao.getImperialWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedTodaysWeather(fetchedWeather : TodaysWeatherResponse){
        //lanch does a job, doesn't return things
        GlobalScope.launch(Dispatchers.IO) {
            todaysWeatherDao.updateAndInsert(fetchedWeather.todaysWeatherEntry)
            weatherLocationDao.updateAndInsert(fetchedWeather.location)
        }
    }
    private suspend fun initWeatherData(){
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if (lastWeatherLocation == null ||
            locationProvider.hasLocationChanged(lastWeatherLocation)){
            fetchTodaysWeather()
            return
        }

        if(isFetchingTodayNeeded(lastWeatherLocation.zonedDateTime))
            fetchTodaysWeather()
    }

    private  suspend fun fetchTodaysWeather(){
        weatherNetworkDataSource.fetchTodaysWeather(
            locationProvider.getPreferedLocationString(),
            Locale.getDefault().language
        )
    }
    private fun isFetchingTodayNeeded(lastFetchTime : ZonedDateTime) : Boolean{
        val halfHourAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(halfHourAgo)
    }
}