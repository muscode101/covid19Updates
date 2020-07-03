package com.muscode.covid19stats.model

import java.security.spec.RSAKeyGenParameterSpec.F0


data class CovidSummary(
    val updated: Long,
    val country: String,
    val countryInfo: CountryInfo,
    val cases: Int,
    val todayCases: Int,
    val deaths: Int,
    val todayDeaths: Int,
    val recovered: Int,
    val active: Int,
    val critical: Int,
    val casesPerOneMillion:Float,
    val deathsPerOneMillion: Float,
    val tests:Int,
    val testsPerOneMillion: Float,
    val continent: String,
    var error:String = ""
) {

    companion object {
        fun empty(error:String = ""): CovidSummary =
            CovidSummary(
                0,
                "",
                CountryInfo(0, "", "", 0f, 0f, ""),
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0f,
                0f,
                0,
                0f,
                "",
                 error
            )
    }
}