package com.example.storyapp.ui.authentication.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.storyapp.R
import com.example.storyapp.ui.BaseFragment
import com.example.storyapp.utils.AppPreferences

class SplashFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            val token = AppPreferences.getToken(requireContext())
            var toMain = false

            val actionId = if (token.isNotEmpty()) {
                toMain= true
                R.id.action_splashFragment_to_mainActivity
            } else {
                R.id.action_splashFragment_to_loginFragment
            }

            findNavController().navigate(actionId)
            if(toMain) requireActivity().finish()
        }, DELAY_TIME)
    }

    companion object {
        private const val DELAY_TIME: Long = 2000L
    }

}