package com.bilibililevel6.home.popular

import com.bilibililevel6.home.popular.entity.PopularData
import com.bilibililevel6.net.BaseDataOfWeb
import com.bilibililevel6.net.BilibiliHost
import com.bilibililevel6.net.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * author：liuzipeng
 * time: 2022/11/15 22:30
 */
class PopularListRepo {
    private val webService by lazy {
        RetrofitManager.createWebService(
            BilibiliHost.WEB_INTEFACE,
            PopularListNetService::class.java
        )
    }

    suspend fun getPopularList() =
        flow {
            emit(webService.getPopularList())
        }.flowOn(Dispatchers.IO)
            .map {
                if (it.code == 400) {
                    throw Exception(it.message)
                }
                it.data
            }
}