package com.example.storyapp.ui

import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.repository.IStoryRepository
import com.example.storyapp.data.model.Result
import com.example.storyapp.data.remote.response.stories.StoriesResponse
import com.example.storyapp.ui.gmap.MapsViewModel
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var storyRepository: IStoryRepository

    private lateinit var mapViewModel: MapsViewModel
    private val dummyStoriesResponse = DataDummy.generateDummyStories()

    @Before
    fun setUp() {
        mapViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when getStoriesWithLocation get called then should return success`() {
        // arrange
        val expectedStoryResponse = MutableLiveData<Result<StoriesResponse>>()
        expectedStoryResponse.value = Result.Success(dummyStoriesResponse)
        `when`(storyRepository.getStoriesWithLocation())
            .thenReturn(expectedStoryResponse)

        // act
        val actualStories = mapViewModel.getStoriesWithLocation().getOrAwaitValue()

        // assert
        Mockito.verify(storyRepository, Mockito.times(1)).getStoriesWithLocation()
        assertNotNull(actualStories)
        assertTrue(actualStories is Result.Success)
        assertEquals(dummyStoriesResponse.listStory.size, (actualStories as Result.Success).data.listStory.size)
    }

    @Test
    fun `should display message error when getStoriesWithLocation return error`() {
        // arrange
        val resultMessage = "network error"
        val expectedStoryResponse = MutableLiveData<Result<StoriesResponse>>()
        expectedStoryResponse.value = Result.Error(resultMessage)
        `when`(storyRepository.getStoriesWithLocation())
            .thenReturn(expectedStoryResponse)

        // act
        val actualStories = mapViewModel.getStoriesWithLocation().getOrAwaitValue()

        // assert
        Mockito.verify(storyRepository, Mockito.times(1)).getStoriesWithLocation()
        assertNotNull(actualStories)
        assertTrue(actualStories is Result.Error)
        assertEquals(resultMessage, (actualStories as Result.Error).error)
    }
}