package com.bilibililevel6.home.popular.intent

/**
 * author：liuzipeng
 * time: 2022/11/20 03:09
 */
sealed class PopularListIntent {
    data class FetchPopularList(val isLoadMore: Boolean = false) : PopularListIntent()
}
