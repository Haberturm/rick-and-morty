package com.haberturm.rickandmorty.data.db.daos.episodes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResultsData

@Dao
interface EpisodesDao {
    @Query("SELECT * FROM episode")
    fun getAllEpisodes(): List<EpisodesResultsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(episodes: ArrayList<CharacterResultsData>)

    @Query("SELECT * FROM episode WHERE id >= :lowerBound AND id <= :upperBound")
    fun getCharactersInRange(lowerBound: Int, upperBound: Int): List<EpisodesResultsData>
}