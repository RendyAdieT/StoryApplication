package com.example.storyapp.utils

import com.example.storyapp.data.remote.response.login.LoginResponse
import com.example.storyapp.data.remote.response.login.LoginResult
import com.example.storyapp.data.remote.response.register.RegisterResponse
import com.example.storyapp.data.remote.response.stories.PostStoryResponse
import com.example.storyapp.data.remote.response.stories.StoriesResponse
import com.example.storyapp.data.remote.response.stories.Story

object DataDummy {
    fun generateDummyStories(): StoriesResponse {
        val listStory = ArrayList<Story>()
        for (i in 1..20) {
            val story = Story(
                createdAt = "2024-05-22T12:00:00Z",
                description = "Deskripsi cerita ke-$i",
                id = "story_id_$i",
                lat = 1.0 * i,
                lon = 2.0 * i,
                name = "Judul Cerita ke-$i",
                photoUrl = "https://example.com/image_$i.jpg"
            )
            listStory.add(story)
        }

        return StoriesResponse(
            error = false,
            message = "Berhasil mengambil cerita",
            listStory = listStory
        )
    }

    fun generateDummyCreateStory(): PostStoryResponse {
        return PostStoryResponse(
            error = false,
            message = "Cerita berhasil diposting"
        )
    }

    fun generateDummyRegister(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "Pendaftaran berhasil"
        )
    }

    fun generateDummyLogin(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "Login berhasil",
            loginResult = LoginResult(
                userId = "user_id_123",
                name = "John Doe",
                token = "example_token"
            )
        )
    }
}
