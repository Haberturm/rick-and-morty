package com.haberturm.rickandmorty.data.db.daos.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResultsData

@Dao
interface CharactersDao {
    @Query("SELECT * FROM character")
    fun getAllCharacters(): List<CharacterResultsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characters: ArrayList<CharacterResultsData>)

    @Query("SELECT * FROM character WHERE id >= :lowerBound AND id <= :upperBound")
    fun getCharactersInRange(lowerBound: Int, upperBound: Int): List<CharacterResultsData>

    @Query("SELECT * FROM character WHERE id = :id")
    fun getCharacterById(id: Int): CharacterResultsData

    @Query("SELECT * FROM character WHERE id IN (:ids)")
    fun getCharactersByIdsList(ids: List<Int>): List<CharacterResultsData>

    @Query("SELECT * FROM character WHERE " +
            "name LIKE '%' || :name || '%' AND " +
            "status LIKE :status AND " +
            "species LIKE '%' || :species || '%' AND " +
            "type LIKE '%' || :type || '%' AND " +
            "gender LIKE :gender")
    fun getFilteredCharacters(
        name: String,
        status:String,
        species: String,
        type: String,
        gender: String
    ): List<CharacterResultsData>

}