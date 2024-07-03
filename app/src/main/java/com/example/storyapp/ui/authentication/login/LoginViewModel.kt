package com.example.storyapp.ui.authentication.login


import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.IStoryRepository

class LoginViewModel(
    private val storyRepository: IStoryRepository
) : ViewModel() {
    fun login(email: String, password: String) =
        storyRepository.postLogin(email, password)
}