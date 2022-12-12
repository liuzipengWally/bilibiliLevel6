package com.bilibililevel6.home.popular

import com.bilibililevel6.BaseRepo
import com.bilibililevel6.extensions.handleResponseCode
import com.bilibililevel6.extensions.transformException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * author：liuzipeng
 * time: 2022/11/15 22:30
 */
class PopularListRepo : BaseRepo() {
    private var pageNum = 1

    suspend fun fetchPopularList(
        isLoadMore: Boolean
    ) = flow {
        if (!isLoadMore) pageNum = 1 else pageNum++
        val popularListResponse =
            webService.fetchPopularList(pageNum, ITEM_COUNT)
        emit(popularListResponse)
    }.handleResponseCode()
        .transformException()
        .flowOn(Dispatchers.IO)

    companion object {
        private const val ITEM_COUNT = 20
    }
}