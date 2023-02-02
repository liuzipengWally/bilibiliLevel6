package com.bilibililevel6.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bilibililevel6.login.state.LoginUiEvent
import com.bilibililevel6.login.state.LoginUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * author：liuzipeng
 * time: 2022/12/20 20:34
 */
class LoginViewModel : ViewModel() {
    private val repo by lazy { LoginRepo() }
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()
    private val _loginUiEvent = Channel<LoginUiEvent>()
    val loginUiEvent: Flow<LoginUiEvent> = _loginUiEvent.receiveAsFlow()

    fun generateQrCode() = viewModelScope.launch {
        repo.generateQrCode().catch {
            it.message?.let { msg ->
                _loginUiEvent.send(LoginUiEvent.GenerateQrCodeFailed(msg))
            }
        }.collect { response ->
            _loginUiState.update {
                it.copy(qrCodeLink = response.url, qrCodeKey = response.qrcode_key)
            }
        }
    }

    fun login(qrCodeKey: String) = viewModelScope.launch {
        repo.login(qrCodeKey).catch {
            it.message?.let { msg ->
                _loginUiEvent.send(LoginUiEvent.ShowToast(msg))
            }
        }.collect {
            when (it.code) {
                QR_CODE_EXPIRE -> _loginUiEvent.send(LoginUiEvent.QrCodeExpire)
                LOGIN_SUCCESS -> _loginUiEvent.send(LoginUiEvent.LoginSuccess(it.refresh_token))
                QR_CODE_NOT_CONFIRM -> _loginUiEvent.send(LoginUiEvent.QrCodeNotConfirm)
            }
        }
    }

    companion object {
        const val LOGIN_SUCCESS = 0
        const val QR_CODE_EXPIRE = 86038
        const val QR_CODE_NOT_CONFIRM = 86090
//        0：扫码登录成功
//        86038：二维码已失效
//        86090：二维码已扫码未确认
//        86101：未扫码
    }
}