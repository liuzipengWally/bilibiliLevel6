package com.bilibililevel6.play.state

import com.bilibililevel6.play.entity.Audio
import com.bilibililevel6.play.entity.SupportFormat
import com.bilibililevel6.play.entity.Video

/**
 * author：liuzipeng
 * time: 2022/11/27 00:12
 */
data class PlayerUiState(
    val supportFormats: List<SupportFormat> = listOf(),
    val videoSources: List<Video> = listOf(),
    val audioSources: List<Audio> = listOf()
)
