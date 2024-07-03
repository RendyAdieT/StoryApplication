package com.example.storyapp.data.remote.retrofit

import android.content.Context
import com.example.storyapp.utils.AppPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val URL = "https://story-api.dicoding.dev/v1/"

        fun getApiService(context: Context): ApiService {
            val token = AppPreferences.getToken(context)

            return Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getInterceptor(token))
                .build()
                .create(ApiService::class.java)
        }

        private fun getInterceptor(token: String?): OkHttpClient {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            return OkHttpClient.Builder()
                .apply {
                    addInterceptor(loggingInterceptor)
                    if (!token.isNullOrEmpty()) {
                        addInterceptor(AuthInterceptor(token))
                    }
                }
                .build()
        }
    }
}