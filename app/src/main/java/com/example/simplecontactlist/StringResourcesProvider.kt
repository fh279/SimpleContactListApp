package com.example.simplecontactlist

import android.content.Context
import androidx.annotation.StringRes

// Это - про DI. Надо изучать. Пока просто работает потому что нашел в гугле.
class StringResourcesProvider (private val context: Context) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}
