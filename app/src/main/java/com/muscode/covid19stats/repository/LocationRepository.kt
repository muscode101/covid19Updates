package com.muscode.covid19stats.repository

import com.muscode.covid19stats.model.DeviceLocation
import com.muscode.covid19stats.repository.retrofit.LocationApi
import retrofit2.HttpException

class LocationRepository(private val deviceLocationApi: LocationApi) {

    suspend fun getLocation(): DeviceLocation = tryToGetLocation()

    private suspend fun tryToGetLocation(): DeviceLocation = try {
        deviceLocationApi.getDeviceLocation()
    } catch (e: Exception) {
        handleException(e)
    }

    private fun handleException(e: Exception): DeviceLocation =
        if (e is HttpException)
            if (e.code() == 504)
                DeviceLocation(error = "Network needed for first run")
            else
                DeviceLocation(error = "network error, check your connection")
        else
            DeviceLocation(error = "network error, check your connection")
}