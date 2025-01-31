package com.kotlinmplibrary.apiframework

import platform.Foundation.NSUserDefaults

class IosStorage: Storage {
    val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults
    override fun getItem(key: String): String {
        return userDefaults.stringForKey(key) ?: "_NONE_"
    }

    override fun setItem(key: String, value: String) {
        userDefaults.setObject(value, key)
    }

    override fun provideContext(context: Any) {
        //Do nothing
    }
}

actual fun getStorage(): Storage = IosStorage()