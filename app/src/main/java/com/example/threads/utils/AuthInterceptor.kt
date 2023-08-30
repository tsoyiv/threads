package com.example.threads.utils

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = preferenceManager.authToken

        if (token != null) {
            val authHeader = "Bearer $token"
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", authHeader)
                .build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(originalRequest)
    }
}
