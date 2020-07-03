package com.muscode.covid19stats.di.component

import com.muscode.covid19stats.di.module.appModule
import com.muscode.covid19stats.view.ui.countries.countriesModule
import com.muscode.covid19stats.view.ui.country.countryModule
import com.muscode.covid19stats.view.ui.splash.splashModule
import org.koin.core.module.Module

val appComponent: List<Module> =
    listOf(
        appModule,
        splashModule,
        countryModule,
        countriesModule
    )