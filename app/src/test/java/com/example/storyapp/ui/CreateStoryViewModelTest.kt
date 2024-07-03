package com.example.storyapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.repository.IStoryRepository
import com.example.storyapp.data.model.Result
import com.example.storyapp.data.remote.response.stories.PostStoryResponse
import com.example.storyapp.ui.main.createstory.CreateStoryViewModel
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateStoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: IStoryRepository

    private lateinit var createStoryViewModel: CreateStoryViewModel
    private val dummyResponse = DataDummy.generateDummyCreateStory()

    @Before
    fun setUp() {
        createStoryViewModel = CreateStoryViewModel(storyRepository)
    }

    @Test
    fun `when postStory get called should return success`() {
        // arrange
        val imageMultipart = mock(MultipartBody.Part::class.java)
        val description = mock(RequestBody::class.java)
        val expectedPostResponse = MutableLiveData<Result<PostStoryResponse>>()
        expectedPostResponse.value = Result.Success(dummyResponse)
        Mockito.`when`(
            storyRepository.postStory(imageMultipart, description)
        ).thenReturn(expectedPostResponse)

        // act
        val actualResponse =
            createStoryViewModel.postStory(imageMultipart, description).getOrAwaitValue()

        // assert
        Mockito.verify(storyRepository, Mockito.times(1))
            .postStory(imageMultipart, description)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(dummyResponse.error, (actualResponse as Result.Success).data.error)
    }

    @Test
    fun `should display error message when postStory return error`() {
        // arrange
        val resultMessage = "network error"
        val imageMultipart = mock(MultipartBody.Part::class.java)
        val description = mock(RequestBody::class.java)
        val expectedPostResponse = MutableLiveData<Result<PostStoryResponse>>()
        expectedPostResponse.value = Result.Error(resultMessage)
        Mockito.`when`(storyRepository.postStory(imageMultipart, description))
            .thenReturn(expectedPostResponse)

        // act
        val actualResponse =
            createStoryViewModel.postStory(imageMultipart, description).getOrAwaitValue()

        // assert
        Mockito.verify(storyRepository, Mockito.times(1)).postStory(imageMultipart, description)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
        assertEquals(resultMessage, (actualResponse as Result.Error).error)
    }
}