package com.kotlinmplibrary.apiframework

class ApiHandler {
    val API_ENDPOINT: String = "https://www.themealdb.com/api/json/v1/1/categories.php"
    val httpClient: HttpClient = getHttpClient()

    suspend fun get(url: String): String {
        return httpClient.get(url)
    }

    fun invalidateCache(url: String) {
        httpClient.invalidateCache(url)
    }

    fun initStorageIfNeeded(context: Any) {
        httpClient.initializeStorageIfNeeded(context)
    }
}