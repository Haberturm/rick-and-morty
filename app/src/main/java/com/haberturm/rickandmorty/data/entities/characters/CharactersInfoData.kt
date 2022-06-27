package com.haberturm.rickandmorty.data.entities.characters

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/* Так как в этой таблице нам интресны только поля charactersCount и charactersPages, которые являются общими для каждой страницы,
то нет, смысла держать строчку в таблице для каждой отдельной страницы. По сути это будет таблица с одно строкой
*/
@Entity(tableName = "charactersInfo")
data class CharactersInfoData (
  @PrimaryKey //в данном случае неважно, что будет являться ключем. Если на сервере обновяться данные, то он заменит нашу единственную строчку
  val key: Int = 1,
  @SerializedName("count" ) var charactersCount : Int,
  @SerializedName("pages" ) var charactersPages : Int,
  @SerializedName("next"  ) var charactersNext  : String?,
  @SerializedName("prev"  ) var charactersPrev  : String?

)