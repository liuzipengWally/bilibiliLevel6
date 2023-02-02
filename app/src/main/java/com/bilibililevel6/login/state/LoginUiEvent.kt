package com.bilibililevel6.login.state

/**
 * author：liuzipeng
 * time: 2022/12/21 19:46
 */
sealed class LoginUiEvent {
    data class GenerateQrCodeFailed(val msg: String) : LoginUiEvent()
    data class ShowToast(val msg: String) : LoginUiEvent()
    data class LoginSuccess(val refreshToken: String) : LoginUiEvent()

    object QrCodeNotConfirm : LoginUiEvent()
    object QrCodeExpire : LoginUiEvent()
}
