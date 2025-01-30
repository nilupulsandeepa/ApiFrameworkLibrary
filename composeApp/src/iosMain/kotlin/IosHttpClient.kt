package com.kotlinmplibrary.apiframework

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.Foundation.HTTPMethod
import platform.Foundation.NSData
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSUserDefaults
import platform.Foundation.dataTaskWithRequest
import platform.posix.memcpy
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IosHttpClient: HttpClient {
    private var userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults

    override suspend fun get(url: String): String {
        if ((userDefaults.stringForKey(url) ?: "_NONE_") == "_NONE_") {
            println("IosHttpClient -> Response From Network")
            return getFromNetwork(url)
        } else {
            println("IosHttpClient -> Response From Storage")
            return (userDefaults.stringForKey(url) ?: "_NONE_").toString()
        }
    }

    override suspend fun getFromNetwork(url: String): String {
        return suspendCoroutine { continuation ->
            val nsURL = NSURL(string = url)
            val request = NSMutableURLRequest(nsURL)
            request.HTTPMethod = "GET"

            val dataTask = NSURLSession.sharedSession().dataTaskWithRequest(request) {
                    data, response, error ->
                if (error != null) {
                    continuation.resumeWithException(Exception("API_REQUEST_FAILED"))
                } else {
                    val statusCode = (response as NSHTTPURLResponse).statusCode
                    if (statusCode in 200..299) {
                        val dataBytes = data?.toByteArray()
                        val responseString = dataBytes?.toStringUtf8()
                        userDefaults.setObject(responseString, url)
                        continuation.resume(responseString ?: "NO_RESPONSE")
                    } else {
                        continuation.resume("NO_RESPONSE")
                    }
                }
            }
            dataTask.resume()
        }
    }

    override fun invalidateCache(url: String) {
        userDefaults.removeObjectForKey(url)
    }

    @OptIn(ExperimentalForeignApi::class)
    fun NSData.toByteArray(): ByteArray {
        val byteArray = ByteArray(this.length.toInt())
        memcpy(byteArray.refTo(0), this.bytes, this.length)
        return byteArray
    }

    private fun ByteArray.toStringUtf8(): String {
        return this.decodeToString()
    }

    override fun initializeStorageIfNeeded(context: Any?) {

    }
}

actual fun getHttpClient(): HttpClient = IosHttpClient()