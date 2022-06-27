package com.haberturm.rickandmorty.presentation.screens.charcters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.mappers.characters.CharactersUiMapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class CharactersMainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<CharacterUi>>>(null)
    val uiState: LiveData<UiState<List<CharacterUi>>>
        get() = _uiState

    init {
        getFilteredData()
    }

    /*
    Изначально идет запрос на обновление данных из интернета. Обрабатывется возможная ошибка связанныя с отсутсвием интренета.
    После обработки в ответа от сети, в любом случае отображаются даннные из бд(если они есть)
     */
    fun getData() {
        viewModelScope.launch {
            _uiState.postValue(UiState.Loading)
            repository.updateCharacters()
                .onEach { networkRequestState ->
                    if (networkRequestState is ApiState.Error) {
                        if (networkRequestState.exception is AppException.NoInternetConnectionException) {
                            _uiState.postValue(UiState.Error(networkRequestState.exception))
                        }
                    }
                    repository.getCharacters()
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<Characters> -> {
                                    _uiState.postValue(
                                        UiState.Data(
                                            CharactersUiMapper().fromDomainToUi<Characters, List<CharacterUi>>(
                                                data.data
                                            )
                                        )
                                    )
                                }
                                is ApiState.Error -> {
                                    Log.e("EXCEPTION", data.exception.toString())
                                    _uiState.postValue(UiState.Error(data.exception))
                                }
                            }
                        }.launchIn(this)
                }
                .launchIn(this)
        }
    }

    fun getFilteredData(){
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.getFilteredCharacters(
                name = "Al",
                status = "unknown",
                species = "Alie",
                type = "",
                gender = ""
            ).onEach {
                Log.i("FILTERED", it.toString())
            }.launchIn(this)
        }
    }


    fun showDetails() {

    }
}