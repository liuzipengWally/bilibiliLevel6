package com.bilibililevel6.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bilibililevel6.home.popular.state.PopularListUiEvent
import com.bilibililevel6.play.intent.PlayerIntent
import com.bilibililevel6.play.state.PlayerUiEvent
import com.bilibililevel6.play.state.PlayerUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * author：liuzipeng
 * time: 2022/11/15 22:26
 */
class PlayerViewModel : ViewModel() {
    private val repo by lazy(LazyThreadSafetyMode.NONE) { PlayerRepo() }
    private val _playerUiState = MutableStateFlow(PlayerUiState())
    val playerUiState: StateFlow<PlayerUiState> = _playerUiState.asStateFlow()
    private val _playerUiEvent = Channel<PlayerUiEvent>()
    val playerUiEvent: Flow<PlayerUiEvent> = _playerUiEvent.receiveAsFlow()

    fun send(intent: PlayerIntent) = when (intent) {
        is PlayerIntent.FetchVideoStreamInfo -> fetchVideoStreamInfo(
            intent.avid,
            intent.cid,
            intent.qn,
            intent.fnval
        )
    }

    private fun fetchVideoStreamInfo(
        avid: Int,
        cid: Int,
        qn: Int,
        fnval: Int
    ) {
        viewModelScope.launch {
            repo.fetchVideoStreamInfo(avid, cid, qn, fnval).catch {
                it.message?.let { msg ->
                    _playerUiEvent.send(PlayerUiEvent.ShowToast(msg))
                }
            }.collect { videoStreamInfo ->
                _playerUiState.update {
                    it.copy(
                        videoSources = videoStreamInfo.dash.video,
                        audioSources = videoStreamInfo.dash.audio,
                        supportFormats = videoStreamInfo.support_formats
                    )
                }
            }
        }
    }
}