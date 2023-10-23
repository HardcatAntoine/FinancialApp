package com.creativeminds.imaginationworld.fantasticodyssey.api

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiProvider private constructor(context: Context) {
    private val gsonConverter = GsonConverterFactory.create()

    private val cacheSize = (10 * 1024 * 1024).toLong()
    private val cache = Cache(context.cacheDir, cacheSize)

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder()
        .cache(cache)
        .addNetworkInterceptor(CacheInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(gsonConverter)
        .build()

    private val api = retrofit.create(ApiService::class.java)
    val apiService: ApiService
        get() = api

    private val retrofit2 = Retrofit
        .Builder()
        .baseUrl(SUB_URL)
        .client(client)
        .addConverterFactory(gsonConverter)
        .build()
    private val apiSub = retrofit2.create(ApiService::class.java)
    val subApiService: ApiService
        get() = apiSub

    fun clearCache() = cache.evictAll()

    companion object {
        const val BASE_URL = "https://herasondad.cyou/"
        const val SUB_URL = "https://analyticsharks.xyz/"

        @Volatile
        private var instance: ApiProvider? = null

        fun getInstance(context: Context): ApiProvider {
            return instance ?: synchronized(this) {
                instance ?: ApiProvider(context.applicationContext).also { instance = it }
            }
        }
    }
}