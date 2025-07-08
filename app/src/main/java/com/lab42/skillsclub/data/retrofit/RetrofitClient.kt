package com.lab42.skillsclub.data.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.NameSpace.SECONDS_15
import com.lab42.skillsclub.data.interceptor.DIDInterceptor
import com.lab42.skillsclub.data.interceptor.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RetrofitClient @Inject constructor(
    didInterceptor: DIDInterceptor,
    tokenInterceptor: TokenInterceptor
) {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(didInterceptor)
        .addInterceptor(loggingInterceptor)
        .readTimeout(SECONDS_15, TimeUnit.SECONDS)
        .connectTimeout(SECONDS_15, TimeUnit.SECONDS)
        .build()

    private val clientWithToken = OkHttpClient.Builder()
        .addInterceptor(didInterceptor)
        .addInterceptor(tokenInterceptor)
        .addInterceptor(loggingInterceptor)
        .readTimeout(SECONDS_15, TimeUnit.SECONDS)
        .connectTimeout(SECONDS_15, TimeUnit.SECONDS)
        .build()

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NameSpace.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
    }

    fun getClientWithToken(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NameSpace.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(clientWithToken)
            .build()
    }
}
