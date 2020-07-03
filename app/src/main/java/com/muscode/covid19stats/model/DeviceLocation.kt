package com.muscode.covid19stats.model

data class DeviceLocation(
    val status: String = "",
    val country: String = "",
    val countryCode: String = "",
    val region: String = "",
    val regionName: String = "",
    val city: String = "",
    val zip: String = "",
    val error: String = ""
)

