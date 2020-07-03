package com.muscode.covid19stats.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkChecker {

    suspend fun isOnline(): Boolean = withContext(Dispatchers.Main) { tryToPing() }

    private suspend fun tryToPing(): Boolean =
        try {
            ping()
            true
        } catch (e: IOException) {
            false
        }

    private suspend fun ping() = withContext(Dispatchers.IO) {
        val sock = Socket()
        sock.run {
            connect(InetSocketAddress("8.8.8.8", 53), 1500)
            close()
        }
    }
}
