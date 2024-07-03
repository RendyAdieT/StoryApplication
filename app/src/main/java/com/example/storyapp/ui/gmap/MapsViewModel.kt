package com.example.storyapp.ui.gmap

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.IStoryRepository

class MapsViewModel(
    private val storyRepository: IStoryRepository
) : ViewModel() {
    fun getStoriesWithLocation() =
        storyRepository.getStoriesWithLocation()
}