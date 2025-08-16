package eu.project.saved.savedWords.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.saved.savedWords.state.DiscardWordDialogState
import eu.project.saved.savedWords.state.SavedWordsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SavedWordsViewModel @Inject constructor(private val savedWordsRepository: SavedWordsRepository): ViewModel() {

    private val _screenState = MutableStateFlow<SavedWordsScreenState>(SavedWordsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val _dialogState = MutableStateFlow<DiscardWordDialogState>(DiscardWordDialogState.Hidden)
    val dialogState = _dialogState.asStateFlow()

    init {

        viewModelScope.launch {

            savedWordsRepository
                .dataState
                .map(::evaluateScreenState)
                .distinctUntilChanged()
                .onEach { newScreenState -> _screenState.update { newScreenState } }
                .collect()
        }
    }



    private fun evaluateScreenState(dataState: SavedWordsRepositoryDataState): SavedWordsScreenState {

        return when(dataState) {
            SavedWordsRepositoryDataState.Loading -> SavedWordsScreenState.Loading
            SavedWordsRepositoryDataState.Loaded.NoData -> SavedWordsScreenState.Loaded.NoData
            is SavedWordsRepositoryDataState.Loaded.Data -> SavedWordsScreenState.Loaded.Data(retrievedData = dataState.retrievedData)
            is SavedWordsRepositoryDataState.FailedToLoad -> SavedWordsScreenState.Error(cause = dataState.cause)
        }
    }



    fun requestWordDeletion(wordToDelete: SavedWord) {

        viewModelScope.launch {

            _dialogState.update { DiscardWordDialogState.Visible(wordToDelete) }
        }
    }

    fun cancelWordDeletion() {

        viewModelScope.launch {

            _dialogState.update { DiscardWordDialogState.Hidden }
        }
    }

    fun deleteWord(wordToDelete: SavedWord) {

        viewModelScope.launch {

            savedWordsRepository.deleteWord(wordToDelete = wordToDelete)
            _dialogState.update { DiscardWordDialogState.Hidden }
        }
    }
}