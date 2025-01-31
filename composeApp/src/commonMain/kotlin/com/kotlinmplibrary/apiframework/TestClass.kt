package com.kotlinmplibrary.apiframework

import kotlinx.serialization.Serializable

@Serializable
data class TestClass (
    val name: String, val age: Int
)