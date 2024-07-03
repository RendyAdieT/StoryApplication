package com.example.storyapp.ui.main.createstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.IStoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(
    private val storyRepository: IStoryRepository
) : ViewModel() {
    fun postStory(file: MultipartBody.Part, description: RequestBody) =
        storyRepository.postStory(file, description)
}