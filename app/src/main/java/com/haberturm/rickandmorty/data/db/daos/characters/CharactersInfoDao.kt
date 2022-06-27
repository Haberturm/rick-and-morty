package com.haberturm.rickandmorty.data.db.daos.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haberturm.rickandmorty.data.entities.characters.CharactersInfoData

@Dao
interface CharactersInfoDao {
    @Query("SELECT * FROM charactersInfo")
    fun getCharactersInfo(): CharactersInfoData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInfo(info: CharactersInfoData)


}