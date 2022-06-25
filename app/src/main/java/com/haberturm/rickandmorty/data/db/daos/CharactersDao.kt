package com.haberturm.rickandmorty.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData

@Dao
interface CharactersDao {
    @Query("SELECT * FROM character")
    fun getAllCharacters(): List<CharacterResultsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characters: ArrayList<CharacterResultsData>)

    @Query("SELECT * FROM character WHERE id >= :lowerBound AND id <= :upperBound")
    fun getCharactersInRange(lowerBound: Int, upperBound: Int): List<CharacterResultsData>

}