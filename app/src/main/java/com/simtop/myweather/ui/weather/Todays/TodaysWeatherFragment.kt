package com.simtop.myweather.ui.weather.Todays

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.simtop.myweather.R
import com.simtop.myweather.data.ApixuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodaysWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = TodaysWeatherFragment()
    }

    private lateinit var viewModel: TodaysWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todays_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TodaysWeatherViewModel::class.java)
        // TODO: Use the ViewModel
        // TODO: Do it in test class
        /*Debug hola, to check if Fetching Data works
        val apiService = ApixuService()
        GlobalScope.launch(Dispatchers.Main) {
            val currentWeatherResponse = apiService.getTodaysWeather("Barcelona").await()
            val hola = currentWeatherResponse.todaysWeatherEntry.toString()
        }
        */
    }
}
