package com.example.storyapp.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder().apply {
                if (token.isNotEmpty()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }.build()

        return chain.proceed(request)
    }

}