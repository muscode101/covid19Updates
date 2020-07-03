//package com.muscode.covid19stats.repository.remote.retrofit
//
//import android.content.Context
//import com.muscode.covid19stats.util.NetworkChecker
//import kotlinx.coroutines.MainScope
//import kotlinx.coroutines.launch
//import okhttp3.Cache
//import okhttp3.Interceptor
//import okhttp3.OkHttpClient
//import okhttp3.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class RetrofitClientInstances(context: Context, private val netChecker: NetworkChecker) {
//
//    private val cacheSize = (10 * 1024 * 1024).toLong()
//    private val myCache = Cache(context.cacheDir, cacheSize)
//    private val covid19BaseUrl = "http://corona.lmao.ninja"
//    private val locationBaseUrl = "http://ip-api.com"
//    private val scope = MainScope()
//    private val myOkHttpClient: OkHttpClient = OkHttpClient.Builder()
//        .cache(myCache)
//        .addInterceptor { chain ->
//            myInterceptor(chain)
//        }
//        .build()
//
//    val locationInstance: Retrofit?
//        get() = Retrofit.Builder()
//            .baseUrl(locationBaseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(myOkHttpClient)
//            .build()
//
//
//    val covidInstance: Retrofit
//        get() = Retrofit.Builder()
//            .baseUrl(covid19BaseUrl)
//            .client(myOkHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//    private fun myInterceptor(chain: Interceptor.Chain): Response {
//        var request = chain.request()
//        if (NetworkChecker().isOnline())
//            request.newBuilder()
//                .header("Cache-Control", "public, max-age=" + 5).build()
//        else
//            try {
//                request.newBuilder().header(
//                    "Cache-Control",
//                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
//                ).build()
//            } catch (e: Exception) {
//                println("httpClientError: $e")
//                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
//            }
////        chain.proceed(request)
//        return chain.proceed(request)
//    }
//
//}