package com.kotlinmplibrary.apiframework

import android.content.Context
import android.content.SharedPreferences
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AndroidHttpClient: HttpClient {
    lateinit private var sharedPreferences: SharedPreferences

    override suspend fun get(url: String): String {
        if (sharedPreferences.getString(url, "_NONE_").equals("_NONE_")) {
            println("AndroidHttpClient -> Response From Network")
            return getFromNetwork(url)
        } else {
            println("AndroidHttpClient -> Response From Storage")
            return sharedPreferences.getString(url, "_NONE_") ?: "_NONE_"
        }
    }

    override suspend fun getFromNetwork(url: String): String {
        return suspendCoroutine { continuation ->
            val connection = URL(url).openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader(InputStreamReader(connection.inputStream)).use {
                        val response = it.readText()
                        sharedPreferences.edit().putString(url, response).apply()
                        continuation.resume(response)
                    }
                } else {
                    continuation.resume("NO_RESPONSE")
                }
            } catch (e: Exception) {
                continuation.resumeWithException(Exception("HTTP Error: ${connection.responseCode}"))
            } finally {
                connection.disconnect()
            }
        }
    }

    override fun invalidateCache(url: String) {
        sharedPreferences.edit().remove(url).apply()
    }

    override fun initializeStorageIfNeeded(context: Any?) {
        sharedPreferences = (context as Context).getSharedPreferences("NetworkCacheStorage", Context.MODE_PRIVATE)
    }
}

actual fun getHttpClient(): HttpClient = AndroidHttpClient()