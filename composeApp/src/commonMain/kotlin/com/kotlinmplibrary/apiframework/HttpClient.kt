package com.kotlinmplibrary.apiframework

interface HttpClient {
    suspend fun get(url: String): String
    suspend fun getFromNetwork(url: String): String
    fun invalidateCache(url: String)
    fun initializeStorageIfNeeded(context: Any?)
}

expect fun getHttpClient(): HttpClient