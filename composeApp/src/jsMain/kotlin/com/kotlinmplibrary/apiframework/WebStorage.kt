package com.kotlinmplibrary.apiframework

import kotlinx.browser.localStorage

class WebStorage: Storage {
    override fun getItem(key: String): String {
        return localStorage.getItem(key) ?: "_NONE_"
    }

    override fun setItem(key: String, value: String) {
        localStorage.setItem(key, value)
    }

    override fun provideContext(context: Any) {
        //Do nothing
    }
}

actual fun getStorage(): Storage = WebStorage()