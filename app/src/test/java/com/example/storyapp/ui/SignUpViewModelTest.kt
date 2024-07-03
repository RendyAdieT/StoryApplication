package com.example.storyapp.ui

import com.example.storyapp.data.remote.response.register.RegisterResponse
import com.example.storyapp.ui.authentication.register.SignUpViewModel
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.repository.IStoryRepository
import com.example.storyapp.data.model.Result
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
class SignUpViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: IStoryRepository

    private lateinit var signUpViewModel: SignUpViewModel
    private val dummySignUpResponse = DataDummy.generateDummyRegister()

    @Before
    fun setUp() {
        signUpViewModel = SignUpViewModel(storyRepository)
    }

    @Test
    fun `when do sign up with correct value then return success`() {
        // arrange
        val expectedSignUpResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedSignUpResponse.value = Result.Success(dummySignUpResponse)
        val name = "name"
        val email = "name@email.com"
        val password = "secretpassword"
        Mockito.`when`(storyRepository.postSignUp(name, email, password))
            .thenReturn(expectedSignUpResponse)

        // act
        val actualResponse = signUpViewModel.signUp(name, email, password).getOrAwaitValue()

        // assert
        Mockito.verify(storyRepository, Mockito.times(1)).postSignUp(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertFalse((actualResponse as Result.Success).data.error)
        assertEquals(
            (expectedSignUpResponse.value as? Result.Success)?.data?.message,
            actualResponse.data.message
        )
    }

    @Test
    fun `when do sign up with incorrect value then return result error`() {
        // arrange
        val resultMessage = "incorrect password value"
        val expectedSignUpResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedSignUpResponse.value = Result.Error(resultMessage)
        val name = "name"
        val email = "name@email.com"
        val password = ""
        Mockito.`when`(storyRepository.postSignUp(name, email, password))
            .thenReturn(expectedSignUpResponse)

        // act
        val actualResponse = signUpViewModel.signUp(name, email, password).getOrAwaitValue()

        // assert
        Mockito.verify(storyRepository, Mockito.times(1)).postSignUp(name, email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
        assertEquals(resultMessage, (actualResponse as Result.Error).error)
    }
}