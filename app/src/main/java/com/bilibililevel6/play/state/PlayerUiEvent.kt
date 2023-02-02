package com.bilibililevel6.play.state

/**
 * author：liuzipeng
 * time: 2022/11/27 00:12
 */
sealed class PlayerUiEvent {
    data class ShowToast(val message: String) : PlayerUiEvent()
}
