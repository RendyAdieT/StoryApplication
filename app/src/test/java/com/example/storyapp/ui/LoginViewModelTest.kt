package com.example.storyapp.ui

import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.repository.IStoryRepository
import com.example.storyapp.data.model.Result
import com.example.storyapp.data.remote.response.login.LoginResponse
import com.example.storyapp.ui.authentication.login.LoginViewModel
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: IStoryRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLogin()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when login with correct value then should return success`() {
        // arrange
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)
        val email = "name@email.com"
        val password = "secretpassword"
        Mockito.`when`(storyRepository.postLogin(email, password))
            .thenReturn(expectedLoginResponse)

        // act
        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()

        // assert
        Mockito.verify(storyRepository, Mockito.times(1)).postLogin(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertFalse((actualResponse as Result.Success).data.error)
        assertEquals(
            (expectedLoginResponse.value as? Result.Success)?.data?.message,
            actualResponse.data.message
        )
        assertEquals(
            (expectedLoginResponse.value as? Result.Success)?.data?.loginResult?.name,
            actualResponse.data.loginResult.name
        )
        assertEquals(
            (expectedLoginResponse.value as? Result.Success)?.data?.loginResult?.userId,
            actualResponse.data.loginResult.userId
        )
        assertEquals(
            (expectedLoginResponse.value as? Result.Success)?.data?.loginResult?.token,
            actualResponse.data.loginResult.token
        )
    }

    @Test
    fun `when login with incorrect value then should return result error`() {
        // arrange
        val resultMessage = "incorrect password value"
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Error(resultMessage)
        val email = "name@email.com"
        val password = ""

        // act
        Mockito.`when`(storyRepository.postLogin(email, password))
            .thenReturn(expectedLoginResponse)

        // assert
        val actualResponse = loginViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(storyRepository, Mockito.times(1)).postLogin(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
        assertEquals(resultMessage, (actualResponse as Result.Error).error)
    }
}