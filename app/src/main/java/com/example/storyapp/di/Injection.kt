package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.repository.IStoryRepository
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.remote.retrofit.ApiConfig

object Injection {

    fun provideRepository(context: Context): IStoryRepository {
        val apiService = ApiConfig.getApiService(context)
        return StoryRepository(apiService)
    }

}