package com.haberturm.rickandmorty.presentation.mappers.characters

import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.mappers.UiMapper
import com.haberturm.rickandmorty.util.Util

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
}