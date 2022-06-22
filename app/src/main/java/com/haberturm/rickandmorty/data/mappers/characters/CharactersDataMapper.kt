package com.haberturm.rickandmorty.data.mappers.characters

import com.haberturm.rickandmorty.data.entities.characters.CharactersResponseData
import com.haberturm.rickandmorty.data.mappers.DataMapper
import com.haberturm.rickandmorty.domain.entities.characters.*
import com.haberturm.rickandmorty.util.Util

class CharactersDataMapper() : DataMapper() {
    @Suppress("UNCHECKED_CAST")
    override fun <T, D> fromDataToDomain(data: T): D {
        if (data !is CharactersResponseData) {
            Util.throwIllegalArgumentException(
                source = "${this::class.qualifiedName}",
                message = "data must be type CharacterResponseData"
            )
        }
        val charactersData = (data as CharactersResponseData)
        return Characters(
            results = charactersData.results.map { resultsData ->
                CharacterResults(
                    id = resultsData.id,
                    name = resultsData.name,
                    status = resultsData.status,
                    species = resultsData.species,
                    type = resultsData.species,
                    gender = resultsData.gender,
                    origin = CharacterOrigin(
                        name = resultsData.origin.name,
                        url = resultsData.origin.url
                    ),
                    location = CharacterLocation(
                        name = resultsData.location.name,
                        url = resultsData.location.url
                    ),
                    image = resultsData.image,
                    episode = resultsData.episode,
                    url = resultsData.url,
                    created = resultsData.created
                )
            } as ArrayList<CharacterResults>,
            info = CharactersInfo(
                count = charactersData.info.count,
                pages = charactersData.info.pages,
                next = charactersData.info.next,
                prev = charactersData.info.prev
            )
        ) as D
    }
}