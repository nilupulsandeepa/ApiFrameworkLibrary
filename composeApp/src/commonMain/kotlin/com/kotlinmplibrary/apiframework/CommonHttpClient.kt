package com.kotlinmplibrary.apiframework

interface CommonHttpClient {
    fun get(url: String, callback: (String) -> Unit)
}

expect fun getHttpClient(): CommonHttpClient