package com.example.storyapp.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.storyapp.R
import com.example.storyapp.data.model.Result
import com.example.storyapp.data.remote.response.register.RegisterResponse
import com.example.storyapp.databinding.FragmentSignUpBinding
import com.example.storyapp.ui.BaseFragment
import com.example.storyapp.utils.ViewModelFactory

class SignUpFragment : BaseFragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val signUpViewModel: SignUpViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvSignupHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }

            btSignUp.setOnClickListener {
                closeKeyboard(it)

                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                signUpViewModel.signUp(name, email, password).observe(requireActivity()) { result ->
                    if (result == null) return@observe
                    checkResult(result)
                }
            }
        }
    }

    private fun checkResult(result: Result<RegisterResponse>) {
        if (result is Result.Loading) {
            displayLoading(true)
            return
        }

        displayLoading(false)

        if (result is Result.Error) {
            showToast(result.error)
            return
        }

        signUp((result as Result.Success).data)
    }


    private fun signUp(data: RegisterResponse) {
        if (data.error) {
            showToast("Daftar akun tidak berhasil!")
            return
        }

        showToast("Daftar akun berhasil!, silahkan login ke aplikasi.")
        findNavController().navigate(
           R.id.action_signUpFragment_to_loginFragment_to_top
        )
    }

    private fun displayLoading(isLoading: Boolean) = with(binding) {
        pbCreateSignup.isVisible = isLoading

        btSignUp.isEnabled = !isLoading
        edRegisterEmail.isEnabled = !isLoading
        edRegisterName.isEnabled = !isLoading
        edRegisterPassword.isEnabled = !isLoading
        textView2.isEnabled = !isLoading
        tvSignupHaveAccount.isEnabled = !isLoading
    }
}