package com.muscode.covid19stats.util


import com.muscode.covid19stats.model.CovidSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyCacheUtil(private val networkChecker: NetworkChecker) {

    var data: List<CovidSummary>? = null

    suspend fun isLoadingFromCache(): Boolean = withContext(Dispatchers.Main) { checkSource() }

    private suspend fun checkSource(): Boolean =
        if (networkChecker.isOnline())
            false
        else
            !networkChecker.isOnline() && !data.isNullOrEmpty()

}
