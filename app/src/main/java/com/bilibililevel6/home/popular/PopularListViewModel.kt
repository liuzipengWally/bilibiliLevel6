package com.bilibililevel6.home.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bilibililevel6.home.popular.intent.PopularListIntent
import com.bilibililevel6.home.popular.state.PopularListUiEvent
import com.bilibililevel6.home.popular.state.PopularListUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * author：liuzipeng
 * time: 2022/11/15 22:26
 */
class PopularListViewModel : ViewModel() {
    private val repo by lazy(LazyThreadSafetyMode.NONE) { PopularListRepo() }
    private val _popularListUiState = MutableStateFlow(PopularListUiState())
    val popularListUiState: StateFlow<PopularListUiState> = _popularListUiState.asStateFlow()
    private val _popularListUiEvent = MutableStateFlow<PopularListUiEvent?>(null)
    val popularListUiEvent: StateFlow<PopularListUiEvent?> = _popularListUiEvent.asStateFlow()

    fun send(intent: PopularListIntent) = when (intent) {
        is PopularListIntent.FetchPopularList -> fetchPopularList(intent.isLoadMore)
    }

    private fun fetchPopularList(isLoadMore: Boolean) {
        viewModelScope.launch {
            repo.fetchPopularList(isLoadMore).onStart {
                _popularListUiState.update {
                    it.copy(isLoading = true)
                }
            }.catch {
                it.message?.let { msg ->
                    if (isLoadMore) {
                        _popularListUiEvent.emit(PopularListUiEvent.ShowToast(msg))
                    } else {
                        _popularListUiEvent.emit(PopularListUiEvent.ShowErrorPage(msg))
                    }
                }
            }.collect { popularData ->
                _popularListUiState.update {
                    it.copy(popularData = popularData, isLoading = false)
                }
            }
        }
    }
}