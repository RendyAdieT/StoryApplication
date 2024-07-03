package com.example.storyapp.ui

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    private val myActivity: BaseActivity? get() = (requireActivity() as? BaseActivity)

    fun closeKeyboard(view: View) {
        myActivity?.closeKeyboard(view)
    }

    fun showToast(message:String?, time: Int = Toast.LENGTH_SHORT) {
        myActivity?.showMessage(message, time)
    }

}