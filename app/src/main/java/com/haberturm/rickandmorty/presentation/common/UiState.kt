package com.haberturm.rickandmorty.presentation.common

import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException

sealed class UiState<out T>{
    data class Data<out T>(val data: T) : UiState<T>()
    data class Error(val exception: AppException) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}
