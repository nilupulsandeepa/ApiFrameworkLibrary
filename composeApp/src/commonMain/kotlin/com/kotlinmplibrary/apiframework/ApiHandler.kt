package com.kotlinmplibrary.apiframework

import kotlinx.serialization.json.Json
import kotlin.js.JsExport

@JsExport
class ApiHandler {
    val API_ENDPOINT: String = "https://www.themealdb.com/api/json/v1/1/categories.php"

    val storage: Storage = getStorage()
    val httpClient: CommonHttpClient = getHttpClient();

    private var cachedMap: MutableMap<String, CacheItem> = mutableMapOf()

    fun fetchData(url: String, callback: (String) -> Unit) {
        var isCachedObjectAvailable: Boolean = false
        val stringObj = storage.getItem("cachedurlobject")
        isCachedObjectAvailable = stringObj != "_NONE_"
        println(stringObj)
        println("Is Cache Available $isCachedObjectAvailable")
        if (isCachedObjectAvailable) {
            cachedMap = Json.decodeFromString(stringObj)
            println(cachedMap[url])
            if (cachedMap[url] != null) {
                println("From Storage")
                callback(cachedMap[url]!!.response)
            } else {
                httpClient.get(url) { response ->
                    println("From Network")
                    cachedMap[url] = CacheItem(url, 123L, response)
                    storage.setItem("cachedurlobject", Json.encodeToString(cachedMap))
                    callback(response)
                }
            }
        } else {
            httpClient.get(url) { response ->
                println("From Network")
                cachedMap[url] = CacheItem(url, 123L, response)
                storage.setItem("cachedurlobject", Json.encodeToString(cachedMap))
                callback(response)
            }
        }
//        val cachedData = storage.getItem(url)
//        if (cachedData != "_NONE_") {
//            println("From Storage")
//            callback(cachedData)
//        } else {
//            httpClient.get(url) { response ->
//                println("From Network")
//                storage.setItem(url, response)
//                callback(response)
//            }
//        }
    }

    fun provideContext(context: Any) {
        storage.provideContext(context)
    }
}