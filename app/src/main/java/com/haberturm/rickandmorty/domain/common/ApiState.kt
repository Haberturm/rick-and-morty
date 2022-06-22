package com.haberturm.rickandmorty.domain.common

sealed class ApiState<out R>{

    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val exception: Exception) : ApiState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
