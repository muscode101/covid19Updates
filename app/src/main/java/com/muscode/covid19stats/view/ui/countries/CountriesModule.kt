package com.muscode.covid19stats.view.ui.countries

import com.muscode.covid19stats.view.adapter.CountryAdapter
import org.koin.dsl.module

val countriesModule = module {
    single { CountryAdapter() }
}