package com.simtop.myweather

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.simtop.myweather.data.db.WeatherDatabase
import com.simtop.myweather.data.network.*
import com.simtop.myweather.data.provider.LocationProvider
import com.simtop.myweather.data.provider.LocationProviderImpl
import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.provider.UnitProviderImpl
import com.simtop.myweather.data.repository.WeatherRepository
import com.simtop.myweather.data.repository.WeatherRepositoryImpl
import com.simtop.myweather.ui.weather.nextdays.details.NextDaysDetailWeatherViewModelFactory
import com.simtop.myweather.ui.weather.nextdays.list.NextDaysListWeatherViewModelFactory
import com.simtop.myweather.ui.weather.todays.TodaysWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class WeatherApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))
        //singleton, because we have only one instance of database
        // we could also do WeatherDatabase.invoke()
        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().todaysWeatherDao() }
        bind() from singleton { instance<WeatherDatabase>().futureWeatherDao() }
        bind() from singleton { instance<WeatherDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixuService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance(),
            instance(),instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { TodaysWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { NextDaysListWeatherViewModelFactory(instance(), instance()) }
        //detailDate is not a constant is allwas different
        bind() from factory { detailDate : LocalDate ->
            NextDaysDetailWeatherViewModelFactory(detailDate, instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        //Set dafalut values that are already set on R.xml.preferences, readAgain false because
        //when the user changes preferences it will keep user info in place of our default set values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}