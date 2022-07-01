package com.haberturm.rickandmorty.presentation.mappers.characters

import com.haberturm.rickandmorty.domain.entities.characters.CharacterResults
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.presentation.entities.CharacterDetailUi
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.mappers.UiMapper
import com.haberturm.rickandmorty.util.Util
import com.haberturm.rickandmorty.util.Util.getIdFromUrl

class CharactersUiMapper : UiMapper() {
    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDomainToUi(data: T): D {
        if (data !is Characters) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type Characters"
            )
        }
        val charactersDomain = (data as Characters)
        return charactersDomain.results.map { character ->
            CharacterUi(
                id = character.id,
                name = character.name,
                status = character.status,
                gender = character.gender,
                species = character.species,
                image = character.image
            )
        } as D
    }

    override fun <T, D> fromDomainToUiSingle(data: T): D {
        if (data !is CharacterResults) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type CharacterResults"
            )
        }
        val characterDomain = data as CharacterResults
        return CharacterDetailUi(
            id = characterDomain.id,
            name = characterDomain.name,
            status = characterDomain.status,
            gender = characterDomain.gender,
            species = characterDomain.species,
            image = characterDomain.image,
            locationName = characterDomain.location.name,
            locationId = getIdFromUrl(characterDomain.location.url),
            originName = characterDomain.origin.name,
            originId = getIdFromUrl(characterDomain.origin.url),
            episodesIds = characterDomain.episode.map { url->
                getIdFromUrl(url)
            }
        ) as D

    }


}