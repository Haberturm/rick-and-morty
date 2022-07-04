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
import com.haberturm.rickandmorty.util.Const
import kotlinx.coroutines.delay
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
    private val _statusPosition = MutableLiveData<Int>(0)
    val statusPosition: LiveData<Int>
        get() = _statusPosition
    private val _statusText = MutableLiveData<String>(statusItems[statusPosition.value!!])
    val statusText: LiveData<String>
        get() = _statusText


    val genderItems = listOf("Not specified", "Female", "Male", "Genderless", "Unknown")
    private val _genderPosition = MutableLiveData<Int>(0)
    val genderPosition: LiveData<Int>
        get() = _genderPosition
    private val _genderText = MutableLiveData<String>(genderItems[0])
    val genderText: LiveData<String>
        get() = _genderText

    private val _currentPage = MutableLiveData<Int>(1)
    val currentPage: LiveData<Int>
        get() = _currentPage

    private val _maxPages = MutableLiveData<Int>(42)
    val maxPages: LiveData<Int>
        get() = _maxPages

    private val _jumpToPageEditState =
        MutableLiveData<Boolean>(false) // false - нет ошибок, true - есть ошибка
    val jumpToPageEditState: LiveData<Boolean>
        get() = _jumpToPageEditState

    private val _nextPageState =
        MutableLiveData<Boolean>(false) // false - нет ошибок, true - есть ошибка
    val nextPageState: LiveData<Boolean>
        get() = _nextPageState

    private val _previousPageState =
        MutableLiveData<Boolean>(false) // false - нет ошибок, true - есть ошибка
    val previousPageState: LiveData<Boolean>
        get() = _previousPageState

    private var filtersApplied = false

    init {
        getData()
    }

    /*
    Изначально идет запрос на обновление данных из интернета. Обрабатывется возможная ошибка связанныя с отсутсвием интренета.
    После обработки в ответа от сети, в любом случае отображаются даннные из бд(если они есть)
     */
    fun getData() {
        val page = currentPage.value!!
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.updateCharacters(page)
                .onEach { networkRequestState ->
                    if (networkRequestState is ApiState.Error) {
                        Log.e("EXCEPTION-network", networkRequestState.exception.toString())
                        if (networkRequestState.exception is AppException.NoInternetConnectionException) {
                            _uiState.postValue(UiState.Error(networkRequestState.exception))
                        }
                    }
                    repository.getCharacters(page)
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<Characters> -> {
                                    delay(500) // for smooth loading screen
                                    if (data.data.results.isEmpty()) {
                                        Log.e("EXCEPTION", "EMPTY_PAGE")
                                        _uiState.postValue(
                                            UiState.Error(
                                                AppException.UnknownException(   //todo: сделать отдельную ошибку на случай пустой бд
                                                    "EMPTY_PAGE"
                                                )
                                            )
                                        )
                                    } else {
                                        _uiState.postValue(
                                            UiState.Data(
                                                CharactersUiMapper().fromDomainToUi<Characters, List<CharacterUi>>(
                                                    data.data
                                                )
                                            )
                                        )
                                        _maxPages.value = data.data.info?.pages
                                    }
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

    private fun makeReturnFormat(string: String?): String? {
        return if (string == "Not specified") {
            ""
        } else {
            string
        }
    }

    fun getFilteredData() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
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
                                pageFilteredData(
                                    CharactersUiMapper().fromDomainToUi<Characters, List<CharacterUi>>(
                                        data.data
                                    )
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

    private fun pageFilteredData(
        list: List<CharacterUi>
    ): List<CharacterUi> {
        val page = currentPage.value!!
        val upperBound = page * Const.ITEMS_PER_PAGE
        val lowerBound = upperBound - Const.ITEMS_PER_PAGE + 1
        _maxPages.value = list.size / 20 + 1
        return list.filterIndexed { index, _ -> index in lowerBound - 1 until upperBound }
    }


    fun speciesTextChanger(text: CharSequence?) {
        _speciesText.value = text?.toString()
    }

    fun typeTextChanger(text: CharSequence?) {
        _typeText.value = text?.toString()
    }

    fun nameTextChanger(text: CharSequence?) {
        _nameText.value = text?.toString()
    }

    fun statusPositionChanger(position: Int) {
        _statusPosition.value = position
        _statusText.value = statusItems[statusPosition.value!!]
    }

    fun genderPositionChanger(position: Int) {
        _genderPosition.value = position
        _genderText.value = genderItems[genderPosition.value!!]
    }

    fun applyFilters() {
        filtersApplied = true
        _currentPage.value = 1
        getFilteredData()
    }

    fun clearFilters() {
        filtersApplied = false
        _currentPage.value = 1
        _genderPosition.value = 0
        _statusPosition.value = 0
        _nameText.value = ""
        _speciesText.value = ""
        _typeText.value = ""
    }

    fun closeFilters(){
        if (!filtersApplied){
            refreshData()
        }
    }

    fun refreshData() {
        if(filtersApplied){
            getFilteredData()
        }else{
            getData()  //в нашем случае, не обязательно перезагружать фрагмент, можно просто обновить данные
        }
    }

    fun nextPage() {
        if (currentPage.value!!.plus(1) > _maxPages.value!!) {
            _nextPageState.value = true
        } else {
            _currentPage.value = currentPage.value?.plus(1)
            _nextPageState.value = false
            if (filtersApplied){
                getFilteredData()
            }else{
                getData()
            }

        }
    }

    fun previousPage() {
        if (currentPage.value!!.minus(1) < 1) {
            _previousPageState.value = true
        } else {
            _currentPage.value = currentPage.value?.minus(1)
            if (filtersApplied){
                getFilteredData()
            }else{
                getData()
            }
        }
    }

    fun jumpToPage(page: CharSequence) {
        try {
            val numberPage = page.toString().toInt()
            if (numberPage in 1.._maxPages.value!!) {
                _currentPage.value = numberPage
                _jumpToPageEditState.value = false
                if (filtersApplied){
                    getFilteredData()
                }else{
                    getData()
                }
            } else {
                _jumpToPageEditState.value = true
            }
        } catch (e: Exception) {
            _jumpToPageEditState.value = true
        }
    }
}