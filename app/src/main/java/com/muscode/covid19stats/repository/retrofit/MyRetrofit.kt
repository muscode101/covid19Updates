package com.muscode.covid19stats.repository.retrofit

import android.content.Context
import android.os.Handler
import android.os.Looper
import okhttp3.*
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class MyRetrofit(context: Context) {


    private val covid19BaseUrl = "http://corona.lmao.ninja"
    private val locationBaseUrl = "http://ip-api.com"
    private var onSetCachedListener: ((Boolean) -> Unit)? = null

    fun setIsCachedListener(isCache: (Boolean) -> Unit) {
        onSetCachedListener = isCache
    }

    private val cacheSize = (10 * 1024 * 1024).toLong()
    private val myCache = Cache(context.cacheDir, cacheSize)
    private val offlineResponse: Interceptor = Interceptor { chain ->
        var request: Request = chain.request()
        request = if (tryToCheckNet()) {
            setIsCacheListener(false)
            request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build()
        } else {
            setIsCacheListener(true)
            request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
        }

        val response: Response = chain.proceed(request)
        println("network: " + response.networkResponse())
        println("cache: " + response.cacheResponse())
        response
    }

    private fun setIsCacheListener(isCached: Boolean) {
        onSetCachedListener?.let { function ->
            function(isCached)
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cache(myCache)
        .addInterceptor(offlineResponse)
        .build()

    val locationInstance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(locationBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    val covidInstance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(covid19BaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun tryToCheckNet() = try {
        val sock = Socket()
        sock.run {
            connect(InetSocketAddress("8.8.8.8", 53), 1500)
            close()
        }
        true
    } catch (e: IOException) {
        false
    } catch (e: HttpException) {
        false
    }
}