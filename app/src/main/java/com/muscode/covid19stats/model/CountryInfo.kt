package com.muscode.covid19stats.model

data class CountryInfo (
	val _id : Int,
	val iso2 : String,
	val iso3 : String,
	val lat : Float,
	val long : Float,
	val flag : String
)