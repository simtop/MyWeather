package com.simtop.myweather.ui.weather.nextdays.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.simtop.myweather.R

class NextDaysListWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = NextDaysListWeatherFragment()
    }

    private lateinit var viewModel: NextDaysListWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_days_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NextDaysListWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
