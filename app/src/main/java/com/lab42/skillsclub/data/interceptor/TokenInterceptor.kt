package com.lab42.skillsclub.data.interceptor

import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.data.SharedPrefInterface
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private var sharedPrefInterface: SharedPrefInterface
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader(NameSpace.TOKEN, sharedPrefInterface.getToken().toString())
            .build()
        return chain.proceed(newRequest)
    }
}