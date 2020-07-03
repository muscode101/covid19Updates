package com.muscode.covid19stats.view.ui.splash

import org.koin.dsl.module

val splashModule = module {
    single { SplashViewModel(get(),get(),get(),get()) }
}