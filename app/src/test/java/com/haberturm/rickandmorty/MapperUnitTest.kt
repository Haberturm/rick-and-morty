package com.haberturm.rickandmorty

import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.episodes.EpisodesInfo
import com.haberturm.rickandmorty.domain.entities.episodes.EpisodesResults
import com.haberturm.rickandmorty.domain.entities.locations.LocationResults
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.entities.locations.LocationsInfo
import com.haberturm.rickandmorty.presentation.entities.EpisodeDetailUi
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi
import com.haberturm.rickandmorty.presentation.mappers.episodes.EpisodesUiMapper
import com.haberturm.rickandmorty.util.Util
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertFailsWith

class MapperUnitTest {

    val urls = arrayListOf<String>(
        "https://rickandmortyapi.com/api/character/1",
        "https://rickandmortyapi.com/api/character/2",
        "https://rickandmortyapi.com/api/character/99",
        "https://rickandmortyapi.com/api/character/123",
        "https://rickandmortyapi.com/api/character/2000",
    )

    val episodesResult = EpisodesResults(
        id = 28,
        name = "The Ricklantis Mixup",
        airDate = "September 10, 2017",
        episode = "S03E07",
        characters = urls,
        url = "https://rickandmortyapi.com/api/episode/28",
        created = "2017-11-10T12:56:36.618Z"
    )
    val episodeDomain = Episodes(
        info = EpisodesInfo(
            count = 51,
            pages = 3,
            next = "https://rickandmortyapi.com/api/episode?page=2",
            prev = null
        ),
        results = arrayListOf(episodesResult)
    )


    val episodeUi = EpisodeUi(
        id = 28,
        name = "The Ricklantis Mixup",
        episode = "S03E07",
        airDate = "September 10, 2017"
    )
    val listEpisodeUi = listOf<EpisodeUi>(episodeUi)

    @Test
    fun `Mapping Episode to EpisodeUi is correct`() {
        assertEquals(listEpisodeUi, EpisodesUiMapper().fromDomainToUi(episodeDomain))
    }

    val ids = listOf<Int>(
        1, 2, 99, 123, 2000
    )
    @Test

    fun `Getting id from api url with getIdFromUrl() is correct`() {
        assertEquals(ids[0], Util.getIdFromUrl(urls[0]))
    }

    @Test
    fun `Mapping list of urls to list of ids with getIdFromUrl() is correct`() {
        assertEquals(
            ids,
            urls.map {
                Util.getIdFromUrl(it)
            }
        )
    }

    val episodeDetailUi = EpisodeDetailUi(
        id = 28,
        name = "The Ricklantis Mixup",
        episode = "S03E07",
        airDate = "September 10, 2017",
        charactersIds = ids
    )
    @Test
    fun `Mapping EpisodeResultsDomain to EpisodeDetailUi is correct`() {
        assertEquals(
            episodeDetailUi,
            EpisodesUiMapper().fromDomainToUiSingle(episodesResult)
        )
    }

    @Test
    fun `Assert passing wrong type to mapper will lead to exception`(){
        val wrongType = Locations(
            info = LocationsInfo(
                count = 51,
                pages = 3,
                next = "https://rickandmortyapi.com/api/episode?page=2",
                prev = null
            ),
            results = arrayListOf(
                LocationResults(
                    id = 1,
                    name = "name",
                    type = "type",
                    dimension = "dimension",
                    residents = arrayListOf("string"),
                    url = "url",
                    created = "created"
                )
            )
        )
        assertFailsWith<IllegalArgumentException>{
            EpisodesUiMapper().fromDomainToUiSingle(wrongType)
        }
    }

}