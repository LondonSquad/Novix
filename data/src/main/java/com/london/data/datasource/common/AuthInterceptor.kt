package com.london.data.datasource.common

import com.london.domain.repository.SessionTokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.annotation.Single

@Single
class AuthInterceptor(
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