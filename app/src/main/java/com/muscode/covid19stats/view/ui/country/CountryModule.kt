package com.muscode.covid19stats.view.ui.country

import com.muscode.covid19stats.commons.CustomProgressDialog
import org.koin.dsl.module


val countryModule = module {
    single { CountryViewModel(get(), get()) }
    single { CustomProgressDialog() }
}