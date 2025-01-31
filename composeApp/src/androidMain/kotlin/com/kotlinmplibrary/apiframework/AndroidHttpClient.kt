package com.kotlinmplibrary.apiframework

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class AndroidHttpClient: CommonHttpClient {
    override fun get(url: String, callback: (String) -> Unit) {
        GlobalScope.launch {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            try {
                urlConnection.requestMethod = "GET"
                val inputStream = urlConnection.inputStream
                val result = inputStream.bufferedReader().use { it.readText() }
                callback(result)
            } catch (e: Exception) {
                callback("API_REQUEST_FAILED: ${e.message}")
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}

actual fun getHttpClient(): CommonHttpClient = AndroidHttpClient()