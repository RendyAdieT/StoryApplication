package com.example.storyapp.ui.main.liststory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.repository.IStoryRepository
import com.example.storyapp.data.remote.response.stories.Story

class ListStoryViewModel(private val storyRepository: IStoryRepository) : ViewModel() {

    val stories: LiveData<PagingData<Story>> get() = storyRepository
        .getStories()
        .cachedIn(viewModelScope)

}