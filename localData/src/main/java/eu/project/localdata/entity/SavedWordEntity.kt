package eu.project.localdata.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.project.common.model.SavedWord
import eu.project.localdata.database.SavedWords
import java.util.UUID

@Entity(tableName = SavedWords)
internal data class SavedWordEntity(
    @PrimaryKey val uuid: UUID,
    val word: String,
    val language: String
)

internal fun SavedWordEntity.convertToModel(): SavedWord =
    SavedWord(
        uuid = this.uuid,
        word = this.word,
        language = this.language
    )

internal fun SavedWord.convertToEntity(): SavedWordEntity =
    SavedWordEntity(
        uuid = this.uuid,
        word = this.word,
        language = this.language
    )