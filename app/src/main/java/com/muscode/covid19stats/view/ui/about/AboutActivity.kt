package com.muscode.covid19stats.view.ui.about

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.muscode.covid19stats.BuildConfig
import com.muscode.covid19stats.R
import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.view.ui.countries.CountriesActivity
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*
import org.koin.android.ext.android.inject


class AboutActivity : AppCompatActivity() {

    private val gson: Gson by inject()

    private val summaryList: List<CovidSummary> by lazy {
        gson.fromJson(
            intent?.extras?.get("countryList").toString(),
            object : TypeToken<List<CovidSummary>>() {}.type
        ) as List<CovidSummary>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViews()

    }



    private fun setupViews() {
        tv_app_version.text =
            getString(R.string.app_version_title).plus(" ")
                .plus(BuildConfig.VERSION_NAME)
        tv_app_name.text = getString(R.string.bold_app_name_title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            backToMain(summaryList)
            return true
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backToMain(summaryList)
    }

    private fun backToMain(summaryList: List<CovidSummary>) {
        val i = Intent(this, CountriesActivity::class.java)
        val gson = Gson()
        i.putExtra("countryList", gson.toJson(summaryList))
        startActivity(i)
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }
}