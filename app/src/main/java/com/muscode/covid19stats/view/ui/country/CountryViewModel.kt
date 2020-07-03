package com.muscode.covid19stats.view.ui.country

import androidx.lifecycle.ViewModel
import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.repository.CovidSummaryRepository
import com.muscode.covid19stats.repository.LocationRepository
import java.util.*

class CountryViewModel(
    private val locationRepository: LocationRepository,
    private val covidSummaryRepository: CovidSummaryRepository
) : ViewModel() {

    private suspend fun getLocation(): String =
        locationRepository.getLocation().country

    suspend fun getCovidSummary(): List<CovidSummary> =
        covidSummaryRepository.getSummary()

    suspend fun getCurrentCountrySummary(): CovidSummary {
        val location = getLocation()
        val covidList = getCovidSummary()
        var covidSummary = CovidSummary.empty()
        covidList.forEach { summary ->
            if (summary.country.toLowerCase(Locale.getDefault()) == location.toLowerCase(Locale.getDefault())) {
                covidSummary = summary
                println("😍😍😍 $summary")
            }
        }
        return covidSummary
    }
}