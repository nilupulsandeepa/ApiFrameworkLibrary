package com.kotlinmplibrary.apiframework

class WebHttpClient: HttpClient {
    override suspend fun get(url: String): String
    override suspend fun getFromNetwork(url: String): String
    override fun invalidateCache(url: String)
    override fun initializeStorageIfNeeded(context: Any?)
}

actual fun getHttpClient(): HttpClient = WebHttpClient()