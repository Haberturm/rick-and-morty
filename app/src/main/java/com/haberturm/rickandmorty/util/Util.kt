package com.haberturm.rickandmorty.util

import java.lang.IllegalArgumentException

object Util {

    fun throwException(source: String, message: String): Nothing{
        throw IllegalArgumentException("Exception in ${source}: '${message}'")
    }
}