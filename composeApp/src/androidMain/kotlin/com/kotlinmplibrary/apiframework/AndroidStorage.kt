package com.kotlinmplibrary.apiframework

import android.content.Context
import android.content.SharedPreferences

class AndroidStorage: Storage {
    lateinit private var sharedPreferences: SharedPreferences

    override fun getItem(key: String): String {
        return sharedPreferences.getString(key, "_NONE_") ?: "_NONE_"
    }

    override fun setItem(key: String, value: String) {

        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun provideContext(context: Any) {
        sharedPreferences = (context as Context).getSharedPreferences("networkcachestorage", Context.MODE_PRIVATE)
    }
}

actual fun getStorage(): Storage = AndroidStorage()