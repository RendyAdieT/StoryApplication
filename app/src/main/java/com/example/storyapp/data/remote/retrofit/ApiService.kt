package com.example.storyapp.data.remote.retrofit

import com.example.storyapp.data.remote.response.login.LoginResponse
import com.example.storyapp.data.remote.response.register.RegisterResponse
import com.example.storyapp.data.remote.response.stories.PostStoryResponse
import com.example.storyapp.data.remote.response.stories.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun postSignUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : StoriesResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int,
    ) : StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): PostStoryResponse
}