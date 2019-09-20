package br.com.githubrepos.library.retrofit

import br.com.githubrepos.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpProvider {
    val instance: OkHttpClient by lazy {
        val httpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            httpClientBuilder.addInterceptor(logger)
        }

        httpClientBuilder.build()
    }
}