package com.example.searchwithdebounce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

data class Item(
    val city: String,
    val description: String
)

class MainViewModel(
    val repository: Repository
): ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Item>> = _searchQuery
        // 1. Time Control: Wait for user to pause typing
        .debounce(300L)
        // 2. Cancellation: Trigger search and cancel previous one if query changes
        .flatMapLatest { query ->
            // Defensive check: don't search if the query is blank
            if (query.isBlank()) flowOf(emptyList<Item>())
            else repository.search(query) // Returns a Flow<List<Item>>
        }
        // 3. Concurrency Control: Run search on the IO dispatcher
        .flowOn(Dispatchers.IO)
        // 4. Hot Flow Conversion: Convert cold flow into hot StateFlow
        .stateIn(
            scope = viewModelScope,
            // Start the search when the UI subscribes, and keep it active
            // for 5s after the last collector disappears (for rotation safety)
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = emptyList() // The initial state
        )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    companion object {
        fun provideFactory(
            repository: Repository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(repository) as T
            }
        }
    }
}