package com.muscode.covid19stats.repository

import android.content.Context
import android.util.Log
import android.view.View
import com.google.android.gms.ads.*
import com.muscode.covid19stats.R
import kotlin.random.Random

class AdmobRepository(private var context: Context) {
    private var adRequest: AdRequest
    private lateinit var bannerAd: AdView
    private lateinit var interstitialAd: InterstitialAd

    init {
        MobileAds.initialize(context, context.getString(R.string.app_admob_id))
        adRequest = AdRequest.Builder().build()
        initInterstitialAd()
    }

    fun loadBannerAds() {
        loadBanner()
    }

    fun showInterstitialAd() {
        if (randomCounter() == 2) {
            loadInterstitial()
        }
    }

    private fun randomCounter(from: Int = 0, to: Int = 3): Int {
        val result = Random.nextInt(to - from) + from
        logger(result.toString())
        return result
    }

    private fun loadInterstitial() {
        if (interstitialAd.isLoaded) {
            interstitialAd.show()
        } else {
            logger("The interstitial wasn't loaded yet.")
            interstitialAd.loadAd(adRequest)
        }
    }

    fun onDestroyAds() {
        bannerAd.destroy()
    }

    fun onPauseAds() {
        bannerAd.pause()
    }

    fun onResumeAds() {
        bannerAd.resume()
    }

    fun onStartAds() {
        loadBanner()
    }

    private fun loadBanner() {
        bannerAd.loadAd(adRequest)
        bannerAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                logger("banner ads loaded")
                bannerAd.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                logger("banner ads failed to load $p0")
                bannerAd.visibility = View.GONE
            }
        }
    }

    private fun initInterstitialAd() {
        interstitialAd = InterstitialAd(context)
        interstitialAd.adUnitId = context.getString(R.string.interstitial_ad_unit_id)
        interstitialAd.loadAd(adRequest)
        initInterstitialAdListener()
    }

    private fun initInterstitialAdListener() {
        interstitialAd.adListener = object : AdListener() {

            override fun onAdFailedToLoad(p0: Int) {
                logger("The interstitial failed to load. $p0")
                interstitialAd.loadAd(adRequest)
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                logger("The interstitial is loaded.")
            }

            override fun onAdClosed() {
                interstitialAd.loadAd(adRequest)
            }
        }
    }

    fun setAdView(bannerAd: AdView) {
        this.bannerAd = bannerAd
    }

    fun logger(message: String = "Admob") {
        Log.d("ADMOB", message)
    }
}