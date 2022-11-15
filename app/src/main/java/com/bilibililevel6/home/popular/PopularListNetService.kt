package com.bilibililevel6.home.popular

import com.bilibililevel6.home.popular.entity.PopularData
import com.bilibililevel6.net.BaseDataOfWeb
import retrofit2.http.GET

/**
 * author：liuzipeng
 * time: 2022/11/15 22:34
 */

interface PopularListNetService {
    @GET("/popular")
    suspend fun getPopularList(): BaseDataOfWeb<PopularData>
}