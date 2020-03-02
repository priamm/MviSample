package com.priamm.mvisample.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import timber.log.Timber

val httpClient = HttpClient(OkHttp) {
    install(JsonFeature){
       serializer = KotlinxSerializer()
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Timber.i(message)
            }
        }
        level = LogLevel.INFO
    }
}