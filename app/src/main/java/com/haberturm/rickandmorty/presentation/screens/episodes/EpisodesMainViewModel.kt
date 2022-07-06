package com.haberturm.rickandmorty.presentation.screens.episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.mappers.characters.CharactersUiMapper
import com.haberturm.rickandmorty.presentation.mappers.episodes.EpisodesUiMapper
import com.haberturm.rickandmorty.util.Const
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class EpisodesMainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<EpisodeUi>>>(null)
    val uiState: LiveData<UiState<List<EpisodeUi>>>
        get() = _uiState

    private val _nameText = MutableLiveData<String>("")
    val nameText: LiveData<String>
        get() = _nameText

    private val _episodesText = MutableLiveData<String>("")
    val episodesText: LiveData<String>
        get() = _episodesText

    private val _currentPage = MutableLiveData<Int>(1)
    val currentPage: LiveData<Int>
        get() = _currentPage

    private val _maxPages = MutableLiveData<Int>(3)
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

    fun getData() {
        val page = currentPage.value!!
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.updateEpisodes(page)
                .onEach { networkRequestState ->
                    if (networkRequestState is ApiState.Error) {
                        Log.e("EXCEPTION-network", networkRequestState.exception.toString())
                        if (networkRequestState.exception is AppException.NoInternetConnectionException) {
                            _uiState.postValue(UiState.Error(networkRequestState.exception))
                        }
                    }
                    repository.getEpisodes(page)
                        .onEach { data ->
                            when (data) {
                                is ApiState.Success<Episodes> -> {
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
                                                EpisodesUiMapper().fromDomainToUi<Episodes, List<EpisodeUi>>(
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


    fun getFilteredData() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.getFilteredEpisodes(
                name = nameText.value ?: "",
                episodes = episodesText.value ?: "",
            ).onEach { data ->
                when (data) {
                    is ApiState.Success<Episodes> -> {
                        if (data.data.results.isEmpty()) {
                            _uiState.postValue(UiState.Error(AppException.NoFilteredData("no filtered data")))
                        } else {
                            _uiState.postValue(
                                UiState.Data(
                                    pageFilteredData(
                                        EpisodesUiMapper().fromDomainToUi<Episodes, List<EpisodeUi>>(
                                            data.data
                                        )
                                    )
                                )
                            )
                        }
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
        list: List<EpisodeUi>
    ): List<EpisodeUi> {
        val page = currentPage.value!!
        val upperBound = page * Const.ITEMS_PER_PAGE
        val lowerBound = upperBound - Const.ITEMS_PER_PAGE + 1
        _maxPages.value = list.size / 20 + 1
        return list.filterIndexed { index, _ -> index in lowerBound - 1 until upperBound }
    }

    fun nameTextChanger(text: CharSequence?) {
        _nameText.value = text?.toString()
    }

    fun episodesTextChanger(text: CharSequence?) {
        _episodesText.value = text?.toString()
    }

    fun applyFilters() {
        if (
            currentPage.value == 1 &&
            nameText.value == "" &&
            episodesText.value == ""
        ) {
            filtersApplied = false
            getData()
        } else {
            filtersApplied = true
            _currentPage.value = 1
            getFilteredData()
        }
    }

    fun clearFilters() {
        filtersApplied = false
        _currentPage.value = 1
        _nameText.value = ""
        _episodesText.value = ""
    }

    fun closeFilters() {
        if (!filtersApplied) {
            refreshData()
        }
    }

    fun refreshData() {
        if (filtersApplied) {
            getFilteredData()
        } else {
            getData()  //в нашем случае, не обязательно перезагружать фрагмент, можно просто обновить данные
        }
    }

    fun nextPage() {
        if (currentPage.value!!.plus(1) > _maxPages.value!!) {
            _nextPageState.value = true
        } else {
            _currentPage.value = currentPage.value?.plus(1)
            _nextPageState.value = false
            if (filtersApplied) {
                getFilteredData()
            } else {
                getData()
            }

        }
    }

    fun previousPage() {
        if (currentPage.value!!.minus(1) < 1) {
            _previousPageState.value = true
        } else {
            _currentPage.value = currentPage.value?.minus(1)
            if (filtersApplied) {
                getFilteredData()
            } else {
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
                if (filtersApplied) {
                    getFilteredData()
                } else {
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