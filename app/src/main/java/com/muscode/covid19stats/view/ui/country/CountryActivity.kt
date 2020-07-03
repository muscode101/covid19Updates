package com.muscode.covid19stats.view.ui.country


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.muscode.covid19stats.R
import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.repository.AdmobRepository
import com.muscode.covid19stats.view.ui.about.AboutActivity
import com.muscode.covid19stats.view.ui.countries.CountriesActivity
import kotlinx.android.synthetic.main.activity_country.*
import kotlinx.android.synthetic.main.content_country.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class CountryActivity : AppCompatActivity(), CoroutineScope {

    private val gson: Gson by inject()
    private val admob: AdmobRepository by inject()
    private var job = Job()
    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main
    private val summaryList: List<CovidSummary> by lazy {
        gson.fromJson(
            intent?.extras?.get("countryList").toString(),
            object : TypeToken<List<CovidSummary>>() {}.type
        ) as List<CovidSummary>
    }
    private val countrySummary: CovidSummary by lazy {
        gson.fromJson(
            intent?.extras?.get("countrySummary").toString(),
            object : TypeToken<CovidSummary>() {}.type
        ) as CovidSummary
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_country)
        setSupportActionBar(toolbar)

        initBannerAds()
        setupAppbar()
        updateUi(countrySummary)

        btn_all_countries.setOnClickListener {
            startListActivity()
        }
    }

    private fun getParentActivity(): String =
        intent?.extras?.get("parent").toString()

    private fun setupAppbar() {
        if (getParentActivity() == "Countries") {
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            println("actionbar showing")
        }
    }

    private fun trySetFlag(imageUrl: String) {
        try {
            setFlag(imageUrl)
        } catch (e: Exception) {
            Log.d("adapter", e.message.toString())
        }
    }

    override fun onBackPressed() {
        startListActivity()
    }

    private fun setFlag(imageUrl: String) {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(this)
            .load(imageUrl)
            .apply(requestOptions)
            .into(iv_flag)
    }

    private fun updateUi(stats: CovidSummary) {
        trySetFlag(stats.countryInfo.flag)
        tv_country.text = stats.country
        tv_active.text = format(stats.active)
        tv_cases.text = format(stats.cases)
        tv_deaths.text = format(stats.deaths)
        tv_recovered.text = format(stats.recovered)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                finish()
                startAboutActivity(summaryList)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            android.R.id.home ->startListActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startAboutActivity(summaryList: List<CovidSummary>) {
        val i = Intent(this, AboutActivity::class.java)
        val gson = Gson()
        i.putExtra("countryList", gson.toJson(summaryList))
        startActivity(i)
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }


    private fun format(number: Int): String =
        NumberFormat.getNumberInstance(Locale.US).format(number)

    private fun startListActivity() {
        val i = Intent(this, CountriesActivity::class.java)
        val gson = Gson()
        i.putExtra("countryList", gson.toJson(summaryList))
        startActivity(i)
    }

    private fun initBannerAds() {
        admob.setAdView(adView)
        admob.loadBannerAds()
    }

    override fun onDestroy() {
        job.cancel()
        admob.onDestroyAds()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        admob.onResumeAds()
    }

    override fun onStart() {
        super.onStart()
        admob.onStartAds()
    }

    override fun onPause() {
        admob.onPauseAds()
        super.onPause()
    }
}