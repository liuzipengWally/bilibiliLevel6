package com.bilibililevel6.play

import com.bilibililevel6.BaseRepo
import com.bilibililevel6.extensions.transformException
import com.bilibililevel6.extensions.handleResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * author：liuzipeng
 * time: 2022/11/15 22:30
 */
class PlayerRepo : BaseRepo() {

    suspend fun fetchVideoStreamInfo(
        avid: Int,
        cid: Int,
        qn: Int,
        fnval: Int = 0,
        fourk: Int = 1,
        fnver: Int = 0
    ) = flow {
        val result = webService.fetchVideoStreamInfo(avid, cid, qn, fourk, fnval, fnver)
        emit(result)
    }.handleResponseCode()
        .transformException()
        .flowOn(Dispatchers.IO)
}