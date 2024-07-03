package com.example.storyapp.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val KEY_PREF = "story_pref"
    private const val KEY_AUTH_TOKEN = "token"

    private fun getInstance(context: Context): SharedPreferences {
        return context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String, context: Context) {
        getInstance(context).edit().apply {
            putString(KEY_AUTH_TOKEN, token)
            apply()
        }
    }

    fun getToken(context: Context): String {
       return getInstance(context).getString(KEY_AUTH_TOKEN, "") ?: ""
    }

    fun removeToken(context: Context) {
        getInstance(context).edit().apply {
            remove(KEY_AUTH_TOKEN)
            apply()
        }
    }
}