package eu.project.localdata.repository

import eu.project.common.localData.SavedWordsDataSource
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SavedWordsRepositoryLocalImpl(
    private val ioCoroutineScope: CoroutineScope,
    private val savedWordsDataSource: SavedWordsDataSource
): SavedWordsRepository {

    private val _dataState = MutableStateFlow<SavedWordsRepositoryDataState>(SavedWordsRepositoryDataState.Loading)
    override val dataState = _dataState.asStateFlow()

    init {

        ioCoroutineScope.launch {

            loadAllWords()
        }
    }


    private val doOnSuccess: (List<SavedWord>) -> Unit = { retrievedData ->

        when(retrievedData.isNotEmpty()) {
            true -> {

                _dataState.value = SavedWordsRepositoryDataState
                    .Loaded
                    .Data(retrievedData = retrievedData)
            }
            false -> {

                _dataState.value = SavedWordsRepositoryDataState
                    .Loaded
                    .NoData
            }
        }
    }

    private val doOnFailure: (Throwable) -> Unit = { error ->

        _dataState.value = SavedWordsRepositoryDataState
            .FailedToLoad(cause = error.message)
    }


    override suspend fun saveWord(wordToSave: SavedWord) {

        savedWordsDataSource.saveWord(wordToSave)
            .onSuccess {

                updateDataState()
            }
            .onFailure {

                // TODO: trigger snackbar class to inform a user about failure
                _dataState.value = SavedWordsRepositoryDataState.FailedToLoad("SaveWord onFailure")
            }
    }

    override suspend fun deleteWord(wordToDelete: SavedWord) {

        savedWordsDataSource.deleteWord(wordToDelete)
            .onSuccess {

                updateDataState()
            }
            .onFailure {

                // TODO: trigger snackbar class to inform a user about failure
                _dataState.value = SavedWordsRepositoryDataState.FailedToLoad("DeleteWord onFailure")
            }
    }

    override suspend fun loadAllWords() {

        _dataState.value = SavedWordsRepositoryDataState.Loading

        savedWordsDataSource.getAllWords()
            .onSuccess(doOnSuccess)
            .onFailure(doOnFailure)
    }

    private suspend fun updateDataState() {

        savedWordsDataSource.getAllWords()
            .onSuccess(doOnSuccess)
            .onFailure(doOnFailure)
    }
}