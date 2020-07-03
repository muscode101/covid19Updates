package com.muscode.covid19stats.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muscode.covid19stats.R
import com.muscode.covid19stats.model.CovidSummary
import com.muscode.covid19stats.view.ui.countries.CountriesActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.rv_country_item.*
import java.text.NumberFormat
import java.util.*


class CountryAdapter : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    private val countryList: ArrayList<CovidSummary> = ArrayList()
    private lateinit var countryArrayList: ArrayList<CovidSummary>
    private var countryClickedListener: ((CovidSummary) -> Unit)? = null


    fun setCountryList(dataList: ArrayList<CovidSummary>) {
        countryArrayList = dataList
    }

    fun setClickListener(country: (CovidSummary) -> Unit) {
        countryClickedListener = country
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_country_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countryArrayList[position].country
        val countrySummary = countryArrayList[position]
        val flag = countrySummary.countryInfo.flag
        val stats = countryArrayList[position]

        holder.tv_country.text = country
        holder.tv_active.text = format(stats.active)
        holder.tv_cases.text = format(stats.cases)
        holder.tv_deaths.text = format(stats.deaths)
        holder.tv_recovered.text = format(stats.recovered)

        holder.card.setOnClickListener {
            countryClickedListener?.let { function ->
                function(countrySummary)
            }
        }
        trySetFlag(holder, flag)
    }

    private fun format(number: Int): String =
        NumberFormat.getNumberInstance(Locale.US).format(number)

    private fun trySetFlag(holder: CountryAdapter.ViewHolder, imageUrl: String) {
        try {
            setFlag(holder, imageUrl)
        } catch (e: Exception) {
            Log.d("adapter", e.message.toString())
        }
    }

    private fun setFlag(holder: CountryAdapter.ViewHolder, imageUrl: String) {
        Glide.with(holder.iv_flag.context)
            .load(imageUrl)
            .into(holder.iv_flag)
    }

    override fun getItemCount(): Int {
        return countryArrayList.size
    }

    fun filter(charText: String) {
        val lowerCaseText = charText.toLowerCase(Locale.getDefault())
        CountriesActivity.countryArrayList!!.clear()
        if (lowerCaseText.isEmpty()) {
            CountriesActivity.countryArrayList!!.addAll(countryList)
        } else {
            for (country in countryList) {
                if (country.country.toLowerCase(Locale.getDefault()).contains(lowerCaseText)) {
                    CountriesActivity.countryArrayList!!.add(country)
                }
            }
        }
        notifyDataSetChanged()
    }

    init {
        countryList.addAll(CountriesActivity.countryArrayList!!)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}