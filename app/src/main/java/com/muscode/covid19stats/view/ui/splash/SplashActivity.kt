package com.muscode.covid19stats.view.ui.splash

import android.R.id
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.google.gson.Gson
import com.muscode.covid19stats.R
import com.muscode.covid19stats.commons.indefiniteSnackbar
import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.view.ui.countries.CountriesActivity
import com.muscode.covid19stats.view.ui.country.CountryActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext


class SplashActivity : AppCompatActivity(), CoroutineScope {

    private var job = Job()
    override val coroutineContext: CoroutineContext get() = job + Dispatchers.Main
    private val viewModel: SplashViewModel by viewModel()
    private var isResponseCached = true

    override fun onCreate(savedInstanceState: Bundle?) {
        removeSupportActionBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        launch {
            delay(3000)
            startActivityWithNetwork()
        }
    }

    private suspend fun startActivityWithNetwork() {
        if (viewModel.isInternetAvailable()) {
            routeToActivity()
        } else {
            viewModel.currentCountrySummary()
            isResponseCached = viewModel.isLoadingCache()
            if (isResponseCached) {
                offlineNotice()
            } else {
                showError("Network error, check your connection")
            }
        }
    }

    private fun routeToActivity() = launch {
        val countrySummary = viewModel.currentCountrySummary()
        val summaryList = viewModel.getCovidSummary()
        if (!countrySummary.country.isNullOrEmpty())
            startCountryActivity(countrySummary, summaryList)
        else {

            val error = summaryList.first().error
            if (error.isNullOrEmpty()) {
                startCountryListActivity(summaryList)
            } else {
                showError(error)
            }
        }
    }

    private fun startCountryListActivity(summaryList: List<CovidSummary>) {
        val i = Intent(this, CountriesActivity::class.java)
        val gson = Gson()
        i.putExtra("countryList", gson.toJson(summaryList))
        startActivity(i)
    }

    private fun removeSupportActionBar() {
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
    }

    private fun showError(error: String) {
        ll_splash.indefiniteSnackbar(
            error,
            "try again"
        ) {
            launch {
                startActivityWithNetwork()
            }
        }
    }

    private fun offlineNotice() {
        showTwoButtonSnackbar()
    }

    private fun showTwoButtonSnackbar(message:String = "network error") {
        val objLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val snackbar = Snackbar.make(
            findViewById(id.content),
            "message",
            Snackbar.LENGTH_INDEFINITE
        )

        val layout = snackbar.view as SnackbarLayout
        val navBarHeight: Int = getNavBarHeight(this)
        val parentParams =
            layout.layoutParams as FrameLayout.LayoutParams
        parentParams.setMargins(0, 0, 0, 0 - navBarHeight + 50)
        layout.layoutParams = parentParams
        layout.setPadding(0, 0, 0, 0)
        layout.layoutParams = parentParams
        val snackView: View = layoutInflater.inflate(R.layout.my_snackbar, null)
        val messageTextView = snackView.findViewById(R.id.message_text_view) as TextView
        messageTextView.text = message
        val btnContinue = snackView.findViewById(R.id.btn_continue) as TextView
        btnContinue.setOnClickListener {
            routeToActivity()
            snackbar.dismiss()
        }
        val btnReconnect = snackView.findViewById(R.id.btn_reconnect) as TextView
        btnReconnect.setOnClickListener {
            launch {
                startActivityWithNetwork()
                snackbar.dismiss()
            }
        }

        layout.addView(snackView, objLayoutParams)
        snackbar.show()
    }

    private fun getNavBarHeight(context: Context): Int {
        var result = 0
        val resourceId: Int =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun startCountryActivity(covidSummary: CovidSummary, summaryList: List<CovidSummary>) {
        val i = Intent(this, CountryActivity::class.java)
        val gson = Gson()
        i.putExtra("countrySummary", gson.toJson(covidSummary))
        i.putExtra("countryList", gson.toJson(summaryList))
        i.putExtra("parent", "Splash")
        i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(i)
        finish()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}