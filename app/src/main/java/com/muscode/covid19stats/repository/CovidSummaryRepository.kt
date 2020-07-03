package com.muscode.covid19stats.repository

import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.repository.retrofit.CovidApi
import retrofit2.HttpException

class CovidSummaryRepository(private val covidSummaryApi: CovidApi) {

    suspend fun getSummary(): List<CovidSummary> = tryToGetSummary()

    private suspend fun tryToGetSummary(): List<CovidSummary> =
        try {
            covidSummaryApi.getCovidSummary()
        } catch (e: Exception) {
            handleException(e)
        }

    private fun handleException(e: Exception): List<CovidSummary> =
        if (e is HttpException)
            if (e.code() == 504)
                listOf(CovidSummary.empty(error = "You are offline"))
            else
                listOf(CovidSummary.empty(error = "network error, check your connection"))
        else
            listOf(CovidSummary.empty(error = "network error, check your connection"))
}