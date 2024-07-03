package com.example.storyapp.ui.authentication.register

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.IStoryRepository

class SignUpViewModel(
    private val storyRepository: IStoryRepository
) : ViewModel() {
    fun signUp(name: String, email: String, password: String) =
        storyRepository.postSignUp(name, email, password)
}