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

    fun getIdFromUrl(url: String): Int {
        if(url.isEmpty()) return -1
        var reversedIdString: String = ""
        run breaking@{
            url.reversed().forEach {
                if (it == '/') return@breaking
                reversedIdString += it
            }
        }
        return reversedIdString.reversed().toInt()
    }

    fun getNewWordPosition(string: String): Int{
        string.forEachIndexed { index, c ->
            if(c == ' ') {
                return index + 1
            }
        }
        return 0
    }
}