package eu.project.common.localData

import eu.project.common.model.SavedWord
import kotlinx.coroutines.flow.StateFlow

interface SavedWordsRepository {

    val dataState: StateFlow<SavedWordsRepositoryDataState>

    suspend fun saveWord(wordToSave: SavedWord)

    suspend fun deleteWord(wordToDelete: SavedWord)

    suspend fun loadAllWords()
}