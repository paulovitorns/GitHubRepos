package br.com.githubrepos.library.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Inject

class RetrofitFactory @Inject constructor() {

    /**
     * Provides a static function to build a new instance of retrofit.
     *
     * @param clazz receive an interface that retrofit will use to consume HTTP requests
     * @param baseUrl is the base API url.
     *
     * @return an implementation made by the Retrofit over the [clazz] interface
     */
    @UnstableDefault
    fun <T> create(clazz: Class<T>, baseUrl: String): T {
        val contentType = MediaType.get("application/json")
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(Json.nonstrict.asConverterFactory(contentType))
            .client(OkHttpProvider.instance)
            .build()

        return retrofit.create(clazz)
    }
}
