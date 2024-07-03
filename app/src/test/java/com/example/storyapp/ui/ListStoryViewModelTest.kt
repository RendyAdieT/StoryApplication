package com.example.storyapp.ui

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.data.repository.IStoryRepository
import com.example.storyapp.data.remote.response.stories.Story
import com.example.storyapp.ui.main.liststory.ListStoryViewModel
import com.example.storyapp.ui.main.liststory.StoriesAdapter
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock private lateinit var storyRepository: IStoryRepository
    private lateinit var listStoryViewModel: ListStoryViewModel

    private var mockStaticLog: MockedStatic<Log>? = null

    private val dummyStoriesResponse = DataDummy.generateDummyStories()

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    @Before
    fun setUp() {
        mockStaticLog=  Mockito.mockStatic(Log::class.java)
        listStoryViewModel = ListStoryViewModel(storyRepository)
    }

    @After
    fun tearDown() {
        mockStaticLog?.close()
    }

    @Test
    fun `when getStories get called should return success`() = runTest {
        // arrange
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStoriesResponse.listStory)
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        Mockito.`when`(storyRepository.getStories())
            .thenReturn(expectedStories)

        // act
        val actualStories: PagingData<Story> = listStoryViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)

        // assert
        Mockito.verify(storyRepository, Mockito.times(1)).getStories()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStoriesResponse.listStory, differ.snapshot())
        Assert.assertEquals(dummyStoriesResponse.listStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStoriesResponse.listStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when getStories get called and return empty data`() = runTest  {
        // arrange`
        val data: PagingData<Story> = StoryPagingSource.snapshot(emptyList())
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        Mockito.`when`(storyRepository.getStories())
            .thenReturn(expectedStories)

        // act
        val actualStories: PagingData<Story> = listStoryViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)

        // assert
        Mockito.verify(storyRepository, Mockito.times(1)).getStories()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertTrue(differ.snapshot().isEmpty())
    }

    class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
        companion object {
            fun snapshot(items: List<Story>): PagingData<Story> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

}
