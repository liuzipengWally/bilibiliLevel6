package com.bilibililevel6.login

import com.bilibililevel6.BaseRepo
import com.bilibililevel6.extensions.handleResponseCode
import com.bilibililevel6.extensions.transformException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * author：liuzipeng
 * time: 2022/12/20 20:36
 */
class LoginRepo : BaseRepo() {
    suspend fun generateQrCode() = flow {
        emit(accountService.generateQrCode())
    }.handleResponseCode()
        .transformException()
        .flowOn(Dispatchers.IO)

    suspend fun login(qrCodeKey: String) = flow {
        emit(accountService.login(qrCodeKey))
    }.handleResponseCode()
        .transformException()
        .flowOn(Dispatchers.IO)
}