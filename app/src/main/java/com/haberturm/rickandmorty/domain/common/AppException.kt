package com.haberturm.rickandmorty.domain.common

sealed class AppException{
    data class NetworkException(val message: String) : AppException()
    data class NoFilteredData(val message: String) : AppException()
    data class NoInternetConnectionException(val message: String) : AppException()
    data class UnknownException(val message: String) : AppException()
}
