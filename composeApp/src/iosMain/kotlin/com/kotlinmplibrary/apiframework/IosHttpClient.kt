package com.kotlinmplibrary.apiframework

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.Foundation.HTTPMethod
import platform.Foundation.NSData
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataTaskWithRequest
import platform.Foundation.setHTTPMethod
import platform.posix.memcpy

class IosHttpClient: CommonHttpClient {

    @OptIn(BetaInteropApi::class)
    override fun get(url: String, callback: (String) -> Unit) {
        GlobalScope.launch {
            val nsUrl = NSURL.URLWithString(url)
            val request = nsUrl?.let { NSMutableURLRequest.requestWithURL(it) }
            request?.setHTTPMethod("GET")

            request?.let {
                NSURLSession.sharedSession.dataTaskWithRequest(it) { data, _, error ->
                    if (error != null) {
                        callback("API_REQUEST_FAILED: ${error.localizedDescription}")
                    } else {
                        val responseText =
                            data?.let { NSString.create(it, NSUTF8StringEncoding)?.toString() }
                        callback(responseText ?: "No response")
                    }
                }
            }?.resume()
        }
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
}


actual fun getHttpClient(): CommonHttpClient = IosHttpClient()