package eu.project.saved.savedWords.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.saved.savedWords.model.DialogViewState
import eu.project.saved.savedWords.model.SavedWordsScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SavedWordsScreenViewModel @Inject constructor(private val savedWordsRepository: SavedWordsRepository): ViewModel() {

    private val _viewState = MutableStateFlow<SavedWordsScreenViewState>(SavedWordsScreenViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _dialogViewState = MutableStateFlow<DialogViewState>(DialogViewState.Hidden)
    val dialogViewState = _dialogViewState.asStateFlow()

    init {

        viewModelScope.launch {

            savedWordsRepository
                .dataState
                .collect {

                    when(it) {

                        SavedWordsRepositoryDataState.Loading ->
                            _viewState.value = SavedWordsScreenViewState.Loading

                        SavedWordsRepositoryDataState.Loaded.NoData ->
                            _viewState.value = SavedWordsScreenViewState.Loaded.NoData

                        is SavedWordsRepositoryDataState.Loaded.Data ->
                            _viewState.value = SavedWordsScreenViewState.Loaded.Data(retrievedData = it.retrievedData)

                        is SavedWordsRepositoryDataState.FailedToLoad ->
                            _viewState.value = SavedWordsScreenViewState.FailedToLoad(cause = it.cause)
                    }
                }
        }
    }

    fun requestWordDeletion(wordToDelete: SavedWord) {

        viewModelScope.launch {

            _dialogViewState.value = DialogViewState.Visible(wordToDelete)
        }
    }

    fun cancelWordDeletion() {

        viewModelScope.launch {

            _dialogViewState.value = DialogViewState.Hidden
        }
    }

    fun deleteWord(wordToDelete: SavedWord) {

        viewModelScope.launch {

            savedWordsRepository.deleteWord(wordToDelete = wordToDelete)
            _dialogViewState.value = DialogViewState.Hidden
        }
    }
}