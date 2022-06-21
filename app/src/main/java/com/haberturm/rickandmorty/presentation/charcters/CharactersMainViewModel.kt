package com.haberturm.rickandmorty.presentation.charcters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.rickandmorty.data.repositories.RepositoryImpl
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.mappers.characters.CharactersUiMapper
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class CharactersMainViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<CharacterUi>>>(null)
    val uiState: LiveData<UiState<List<CharacterUi>>>
        get() = _uiState


    init {
        viewModelScope.launch {
            _uiState.postValue(UiState.Loading)
            when(val data = repository.getCharacters()){
                is ApiState.Success<Characters> -> {
                    Log.i("DATA", data.data.toString())
                    _uiState.postValue(
                        UiState.Data(CharactersUiMapper().fromDomainToUi<Characters, List<CharacterUi>>(data.data))
                    )
                }
                is ApiState.Error -> {
                    Log.i("DATA", data.exception.toString())
                    UiState.Error(Exception(data.exception))
                }
            }

            //Log.i("DATA", "${CharactersUiMapper().fromDomainToUi<Characters, CharacterUi>(repository.getCharacters())}")
        }
    }

    fun getData(){
        viewModelScope.launch {
            _uiState.postValue(UiState.Loading)
            when(val data = repository.getCharacters()){
                is ApiState.Success<Characters> -> {
                    _uiState.postValue(
                        UiState.Data(CharactersUiMapper().fromDomainToUi<Characters, List<CharacterUi>>(data.data))
                    )
                }
                is ApiState.Error -> {
                    UiState.Error(Exception(data.exception))
                }
            }

            //Log.i("DATA", "${CharactersUiMapper().fromDomainToUi<Characters, CharacterUi>(repository.getCharacters())}")
        }
    }


    fun showDetails(){

    }

    fun getRepo(): Repository {
        return RepositoryImpl()
    }

    val list = listOf<CharacterUi>(
        CharacterUi(
            0,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
           " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            1,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            2,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            3,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            4,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
    )
}