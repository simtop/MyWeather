package com.simtop.myweather

import android.app.Application
import com.simtop.myweather.data.db.TodaysWeatherDao
import com.simtop.myweather.data.db.WeatherDatabase
import com.simtop.myweather.data.network.*
import com.simtop.myweather.data.repository.WeatherRepository
import com.simtop.myweather.data.repository.WeatherRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class WeatherApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))
        //singleton, because we have only one instance of database
        // we could also do WeatherDatabase.invoke()
        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixuService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance()) }

    }
}