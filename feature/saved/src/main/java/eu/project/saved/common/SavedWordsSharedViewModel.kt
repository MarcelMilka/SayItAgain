package eu.project.saved.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedWordsSharedViewModel @Inject constructor(): ViewModel() {

    val listState = LazyListState()
}