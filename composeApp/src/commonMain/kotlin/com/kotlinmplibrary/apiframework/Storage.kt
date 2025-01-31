package com.kotlinmplibrary.apiframework

interface Storage {
    fun getItem(key: String): String
    fun setItem(key: String, value: String)
    fun provideContext(context: Any)
}

expect fun getStorage(): Storage