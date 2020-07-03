package com.muscode.covid19stats.view.ui.countries

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.muscode.covid19stats.R
import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.repository.AdmobRepository
import com.muscode.covid19stats.view.adapter.CountryAdapter
import com.muscode.covid19stats.view.ui.about.AboutActivity
import com.muscode.covid19stats.view.ui.country.CountryActivity
import kotlinx.android.synthetic.main.activity_countries.adView
import kotlinx.android.synthetic.main.activity_countries.toolbar
import kotlinx.android.synthetic.main.content_countries.*
import org.koin.android.ext.android.inject
import kotlin.system.exitProcess

class CountriesActivity : AppCompatActivity() {

    private val gson: Gson by inject()
    private val countryAdapter: CountryAdapter by inject()
    private val admob: AdmobRepository by inject()

    companion object {
        var countryArrayList: ArrayList<CovidSummary>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)
        setSupportActionBar(toolbar)

        initBannerAds()
        initCountryArrayList()
        initSearchBar()
        initRecyclerView()
        getClickedItem()

    }

    private fun initCountryArrayList() {
        countryArrayList = getSummaryList() as ArrayList<CovidSummary>
    }

    private fun initSearchBar() {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                println(query)
                countryAdapter.filter(query)
            }
        }
    }

    private fun getSummaryList(): List<CovidSummary> = gson.fromJson(
        intent?.extras?.get("countryList").toString(),
        object : TypeToken<List<CovidSummary>>() {}.type
    )

    private fun initRecyclerView() {
        countryAdapter.setCountryList(countryArrayList!!)
        rv_list.adapter = countryAdapter
    }

    private fun getClickedItem() {
        countryAdapter.setClickListener { countrySummary ->
            startCountryActivity(countrySummary, countryArrayList!!.toList())
        }
    }

    private fun startCountryActivity(covidSummary: CovidSummary, summaryList: List<CovidSummary>) {
        admob.showInterstitialAd()
        val i = Intent(this, CountryActivity::class.java)
        val gson = Gson()
        i.putExtra("countrySummary", gson.toJson(covidSummary))
        i.putExtra("countryList", gson.toJson(summaryList))
        i.putExtra("parent", "Countries")
        startActivity(i)
        finish()
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

    override fun onBackPressed() {
        this.finishAffinity()
        exitProcess(0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                countryAdapter.filter(query!!)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                countryAdapter.filter(query!!)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                startAboutActivity(countryArrayList!!.toList())
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBannerAds() {
        admob.setAdView(adView)
        admob.loadBannerAds()
    }

    override fun onDestroy() {
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