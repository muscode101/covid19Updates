package com.muscode.covid19stats.repository.retrofit

import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.model.DeviceLocation
import retrofit2.http.GET

interface CovidApi {
    @GET("/v2/countries?sort=country")
    suspend fun getCovidSummary(): List<CovidSummary>
}

interface LocationApi {
    @GET("/json")
    suspend fun getDeviceLocation(): DeviceLocation
}
