package com.haberturm.rickandmorty.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.lang.IllegalArgumentException

object Util {

    fun throwIllegalArgumentException(source: String, message: String): Nothing {
        throw IllegalArgumentException("Exception in ${source}: '${message}'")
    }

    fun hideKeyboard(context: Context) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = (context as Activity).currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }
}