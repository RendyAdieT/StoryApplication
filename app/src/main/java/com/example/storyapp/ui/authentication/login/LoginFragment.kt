package com.example.storyapp.ui.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.storyapp.R
import com.example.storyapp.data.model.Result
import com.example.storyapp.data.remote.response.login.LoginResponse
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.ui.BaseFragment
import com.example.storyapp.utils.AppPreferences
import com.example.storyapp.utils.ViewModelFactory

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvLoginDontHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }

            btLogin.setOnClickListener {
                closeKeyboard(it)

                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()

                loginViewModel.login(email, password).observe(requireActivity()) { result ->
                    if (result == null) return@observe
                    checkResult(result)
                }
            }
        }
    }

    private fun checkResult(result: Result<LoginResponse>) {
        if (result is Result.Loading) {
            displayLoading(true)
            return
        }

        displayLoading(false)

        if (result is Result.Error) {
            showToast(result.error)
            return
        }

        login((result as Result.Success).data)
    }

    private fun displayLoading(isLoading: Boolean) = with(binding) {
        pbLogin.isVisible = isLoading

        btLogin.isEnabled = !isLoading
        edLoginEmail.isEnabled = !isLoading
        edLoginPassword.isEnabled = !isLoading
        textView6.isEnabled = !isLoading
        tvLoginDontHaveAccount.isEnabled = !isLoading
    }

    private fun login(data: LoginResponse) {
        if (data.error) {
            showToast(data.message)
            return
        }

        AppPreferences.saveToken(data.loginResult.token, requireContext())
        findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        requireActivity().finish()
    }
}