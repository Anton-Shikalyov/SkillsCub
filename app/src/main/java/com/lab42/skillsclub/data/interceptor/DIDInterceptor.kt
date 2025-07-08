package com.lab42.skillsclub.data.interceptor

import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.domain.use_cases.api_service_use_case.GetDeviceIDUseCase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DIDInterceptor @Inject constructor(
    private var getDeviceIDUseCase: GetDeviceIDUseCase
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader(NameSpace.DID, getDeviceIDUseCase.execute())
            .build()
        return chain.proceed(newRequest)
    }
}