package com.example.caseapp.network

import android.content.Context
import com.example.caseapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor(context: Context) {

    private val apiService: ApiService

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getApiService(): ApiService = apiService

    companion object {
        private const val BASE_URL = "https://api.example.com/" // Cambiar por la URL real de la API

        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance(context: Context): ApiService {
            return instance?.getApiService() ?: synchronized(this) {
                instance?.getApiService() ?: RetrofitClient(context.applicationContext).also {
                    instance = it
                }.getApiService()
            }
        }
    }
}