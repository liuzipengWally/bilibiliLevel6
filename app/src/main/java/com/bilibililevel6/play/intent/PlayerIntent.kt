package com.bilibililevel6.play.intent

/**
 * author：liuzipeng
 * time: 2022/11/20 03:09
 */
sealed class PlayerIntent {
    data class FetchVideoStreamInfo(
        val avid: Int,
        val cid: Int,
        val qn: Int = 0,
        val fnval: Int = 80
    ) : PlayerIntent()
}
