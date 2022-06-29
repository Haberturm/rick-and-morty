package com.haberturm.rickandmorty.data.db.daos.episodes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationResultsData

@Dao
interface EpisodesDao {
    @Query("SELECT * FROM episode")
    fun getAllEpisodes(): List<EpisodesResultsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(episodes: ArrayList<EpisodesResultsData>)

    @Query("SELECT * FROM episode WHERE id >= :lowerBound AND id <= :upperBound")
    fun getEpisodesInRange(lowerBound: Int, upperBound: Int): List<EpisodesResultsData>

    @Query("SELECT * FROM episode WHERE " +
            "name LIKE '%' || :name || '%' AND " +
            "episode LIKE '%' || :episodes || '%' ")
    fun getFilteredEpisodes(
        name: String,
        episodes: String,
    ): List<EpisodesResultsData>
}