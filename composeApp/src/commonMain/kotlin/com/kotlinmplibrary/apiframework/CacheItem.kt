package com.kotlinmplibrary.apiframework

import kotlinx.serialization.Serializable

@Serializable
data class CacheItem (
    val url: String, val timestamp: Long, val response: String
)