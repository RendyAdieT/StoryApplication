package com.example.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.storyapp.data.model.Result
import com.example.storyapp.data.remote.response.login.LoginResponse
import com.example.storyapp.data.remote.response.register.RegisterResponse
import com.example.storyapp.data.remote.response.stories.PostStoryResponse
import com.example.storyapp.data.remote.response.stories.StoriesResponse
import com.example.storyapp.data.remote.response.stories.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IStoryRepository {
    fun getStories(): LiveData<PagingData<Story>>

    fun postStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<PostStoryResponse>>

    fun postSignUp(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>>

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>>

    fun getStoriesWithLocation(): LiveData<Result<StoriesResponse>>
}