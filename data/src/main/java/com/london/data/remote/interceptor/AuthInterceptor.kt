package com.london.data.remote.interceptor

import com.london.domain.repository.SessionTokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenProvider: SessionTokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        tokenProvider.getAuthKey()?.let { authKey ->
            builder.addHeader("Authorization", "Bearer $authKey")
        }
        return chain.proceed(builder.build())
    }
}