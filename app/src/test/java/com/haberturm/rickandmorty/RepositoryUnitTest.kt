package com.haberturm.rickandmorty

import com.haberturm.rickandmorty.data.db.RickAndMortyDatabase
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResultsData
import com.haberturm.rickandmorty.data.repositories.RepositoryImpl
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.episodes.EpisodesResults
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class RepositoryUnitTest {
    @get:Rule
    var coroutineRule = CoroutineRule()
    private val testDispatcher = TestCoroutineDispatcher()
    private val db = mockk<RickAndMortyDatabase>()
    lateinit var repository: RepositoryImpl
    private val episodeIdToTest = 28
    private val episodeErrorId = 666


    @Before
    fun initRepository() {
        initDB()
        repository = RepositoryImpl(db, testDispatcher)
    }

    private fun initDB() {
        val urls = arrayListOf<String>(
            "https://rickandmortyapi.com/api/character/1",
            "https://rickandmortyapi.com/api/character/2",
            "https://rickandmortyapi.com/api/character/99",
            "https://rickandmortyapi.com/api/character/123",
            "https://rickandmortyapi.com/api/character/2000",
        )

        val singleEpisode = EpisodesResultsData(
            id = 28,
            name = "The Ricklantis Mixup",
            airDate = "September 10, 2017",
            episode = "S03E07",
            characters = urls,
            url = "https://rickandmortyapi.com/api/episode/28",
            created = "2017-11-10T12:56:36.618Z"
        )
        every { db.episodesDao().getEpisodeById(episodeIdToTest) } returns singleEpisode
        every { db.episodesDao().getEpisodeById(episodeErrorId) } returns null
    }


    @Test
    fun `Get single episode`() = runBlockingTest {
        val urls = arrayListOf<String>(
            "https://rickandmortyapi.com/api/character/1",
            "https://rickandmortyapi.com/api/character/2",
            "https://rickandmortyapi.com/api/character/99",
            "https://rickandmortyapi.com/api/character/123",
            "https://rickandmortyapi.com/api/character/2000",
        )
        val episode = EpisodesResults(
            id = 28,
            name = "The Ricklantis Mixup",
            airDate = "September 10, 2017",
            episode = "S03E07",
            characters = urls,
            url = "https://rickandmortyapi.com/api/episode/28",
            created = "2017-11-10T12:56:36.618Z"
        )

        //onEach для полной симуляции того, как это сделано во вью модели
        repository.getSingleEpisode(episodeIdToTest)
            .onEach {
                assertEquals(
                    ApiState.Success(episode),
                    it
                )
            }
            .launchIn(this)
    }

    @Test
    fun `Get non-existent episode`() = runBlockingTest {
        repository.getSingleEpisode(episodeErrorId)
            .onEach {
                assertEquals(
                    ApiState.Error(AppException.UnknownException("null cannot be cast to non-null type com.haberturm.rickandmorty.data.entities.episodes.EpisodesResultsData")),
                    it
                )
            }
            .launchIn(this)
    }
}


