package com.muscode.covid19stats.view.ui.splash

import androidx.lifecycle.ViewModel
import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.repository.CovidSummaryRepository
import com.muscode.covid19stats.repository.LocationRepository
import com.muscode.covid19stats.util.MyCacheUtil
import com.muscode.covid19stats.util.NetworkChecker
import java.util.*

class SplashViewModel(
    private val networkChecker: NetworkChecker,
    private val locationRepository: LocationRepository,
    private val covidSummaryRepository: CovidSummaryRepository,
    private val myCacheUtil: MyCacheUtil
) : ViewModel() {
    private var data: List<CovidSummary>? = null

    private suspend fun getLocation(): String =
        locationRepository.getLocation().country

    suspend fun getCovidSummary(): List<CovidSummary> =
        covidSummaryRepository.getSummary()

    suspend fun isInternetAvailable(): Boolean =
        networkChecker.isOnline()

    suspend fun currentCountrySummary(): CovidSummary {
        val location = getLocation()
        val covidList = getCovidSummary()
        var covidSummary = CovidSummary.empty()
        data = covidList
        covidList.forEach { summary ->
            if (summary.country.toLowerCase(Locale.getDefault()) == location.toLowerCase(Locale.getDefault())) {
                covidSummary = summary
            }
        }
        return covidSummary
    }

    suspend fun isLoadingCache(): Boolean {
        myCacheUtil.data = data
        return myCacheUtil.isLoadingFromCache()
    }
}