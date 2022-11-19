package com.bilibililevel6.home.popular.state

/**
 * author：liuzipeng
 * time: 2022/11/19 00:26
 */
sealed class PopularListUiEvent {
    data class ShowToast(val message: String) : PopularListUiEvent()
    data class ShowErrorPage(val message: String) : PopularListUiEvent()
}
