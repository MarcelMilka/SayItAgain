package eu.project.localdata.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.project.localdata.entity.SavedWordEntity

@Dao
internal interface SavedWordDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: SavedWordEntity)

    @Delete
    suspend fun deleteWord(word: SavedWordEntity)

    @Query("SELECT * FROM SAVED_WORDS")
    suspend fun selectAllWords(): List<SavedWordEntity>
}