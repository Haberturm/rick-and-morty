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
import javax.inject.Inject

class CharactersMainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<CharacterUi>>>(null)
    val uiState: LiveData<UiState<List<CharacterUi>>>
        get() = _uiState

    private val _nameText = MutableLiveData<String>("")
    val nameText: LiveData<String>
        get() = _nameText

    private val _speciesText = MutableLiveData<String>("")
    val speciesText: LiveData<String>
        get() = _speciesText

    private val _typeText = MutableLiveData<String>("")
    val typeText: LiveData<String>
        get() = _typeText

    val statusItems = listOf("Not specified", "Alive", "Dead", "Unknown")
    private val _statusText = MutableLiveData<String>(statusItems[0])
    val statusText: LiveData<String>
        get() = _statusText

    val genderItems = listOf("Not specified", "Female", "Male", "Genderless", "Unknown")
    private val _genderText = MutableLiveData<String>(genderItems[0])
    val genderText: LiveData<String>
        get() = _genderText

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

    private fun makeReturnFormat(string: String?): String?{
        return if(string == "Not specified"){
            ""
        }else{
            string
        }
    }

    fun getFilteredData() {
        _uiState.value = UiState.Loading


        viewModelScope.launch {
            Log.i("REQUEST", "${nameText.value ?: ""}; ${statusText.value ?: ""}, ${speciesText.value ?: ""}, ${typeText.value ?: ""}; ${genderText.value ?: ""}")
            repository.getFilteredCharacters(
                name = nameText.value ?: "",
                status = makeReturnFormat(statusText.value) ?: "",
                species = speciesText.value ?: "",
                type = typeText.value ?: "",
                gender = makeReturnFormat(genderText.value) ?: ""
            ).onEach { data ->
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
    }


    fun speciesTextChanger(text: String?) {
        _speciesText.value = text
    }

    fun typeTextChanger(text: String?) {
        _typeText.value = text
    }

    fun nameTextChanger(text: String?) {
        _nameText.value = text
    }

    fun statusTextChanger(text: String) {
        _statusText.value = text
    }

    fun genderTextChanger(text: String?) {
        _genderText.value = text
    }


    fun showDetails() {

    }
}