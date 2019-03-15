package com.simtop.myweather.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.simtop.myweather.data.db.entity.WeatherLocation
import com.simtop.myweather.internal.LocationPermissionNotGrantedException
import com.simtop.myweather.internal.asDeferred
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val COSTUM_LOCATION = "COSTUM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context : Context) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e : LocationPermissionNotGrantedException){
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    override suspend fun getPreferedLocationString(): String {

        if(isUsingDeviceLocation()){
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e : LocationPermissionNotGrantedException){
                return "${getCustomLocationName()}"
            }
        }
        else
            return "${getCustomLocationName()}"

    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            //if it's null return false
            ?: return false

        // Comparing doubles cannot be done with "=="
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    //Kotlin did not detect my method hasLocationPermission for that reason I did @SuppresLint
    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if(!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherLocation.name
        }
        return false
    }

    private fun getCustomLocationName(): String? {
        //CUSTOM Typo
        return preferences.getString(COSTUM_LOCATION, null)
    }
}