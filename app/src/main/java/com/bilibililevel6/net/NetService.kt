package com.bilibililevel6.net

import com.bilibililevel6.home.popular.entity.PopularData
import com.bilibililevel6.login.entity.LoginResultResponse
import com.bilibililevel6.login.entity.QrCodeResponse
import com.bilibililevel6.play.entity.VideoStreamInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * author：liuzipeng
 * time: 2022/11/15 22:34
 */

interface NetService {
    @GET("web-interface/popular")
    suspend fun fetchPopularList(
        @Query("pn") pageNum: Int,
        @Query("ps") itemCount: Int
    ): BaseDataOfWeb<PopularData>

    @GET("player/playurl")
    suspend fun fetchVideoStreamInfo(
        @Query("avid") avid: Int,
        @Query("cid") cid: Int,
        @Query("qn") qn: Int,
        @Query("fourk") fourk: Int,
        @Query("fnval") fnval: Int = 0,
        @Query("fnver") fnver: Int = 0): BaseDataOfWeb<VideoStreamInfo>

    @GET("web/qrcode/generate")
    suspend fun generateQrCode(): BaseDataOfWeb<QrCodeResponse>

    @GET("web/qrcode/poll")
    suspend fun login(@Query("qrcode_key") qrCodeKey: String): BaseDataOfWeb<LoginResultResponse>
}