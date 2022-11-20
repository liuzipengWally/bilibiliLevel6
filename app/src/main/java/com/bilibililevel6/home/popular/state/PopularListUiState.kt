package com.bilibililevel6.home.popular.state

import com.bilibililevel6.home.popular.entity.PopularData

/**
 * author：liuzipeng
 * time: 2022/11/19 00:25
 */
data class PopularListUiState(
    val isLoading: Boolean = false,
    val isLoadMore: Boolean = false,
    val popularData: PopularData? = null
) {
    val loadMorePopularData get() = if (isLoadMore) popularData else null
}
