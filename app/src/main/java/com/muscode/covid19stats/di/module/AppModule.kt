package com.muscode.covid19stats.di.module


import android.content.SharedPreferences
import com.google.gson.Gson
import com.muscode.covid19stats.repository.AdmobRepository
import com.muscode.covid19stats.repository.CovidSummaryRepository
import com.muscode.covid19stats.repository.LocationRepository
import com.muscode.covid19stats.repository.retrofit.CovidApi
import com.muscode.covid19stats.repository.retrofit.LocationApi
import com.muscode.covid19stats.repository.retrofit.MyRetrofit
import com.muscode.covid19stats.util.MyCacheUtil
import com.muscode.covid19stats.util.NetworkChecker
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    factory { Gson() }
    single { NetworkChecker() }
    single { LocationRepository(get()) }
    single { CovidSummaryRepository(get()) }
    single { MyRetrofit(get()) }
    single { AdmobRepository(get()) }
    single<SharedPreferences> { androidApplication().getSharedPreferences("Global",0)}
    single {
        MyRetrofit(get()).covidInstance.create(CovidApi::class.java)
    }
    single {
        MyRetrofit(get()).locationInstance.create(LocationApi::class.java)
    }


    single {
        MyCacheUtil(get())
    }
}
