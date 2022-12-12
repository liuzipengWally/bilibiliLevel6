package com.bilibililevel6

import com.bilibililevel6.net.BilibiliHost
import com.bilibililevel6.net.NetService
import com.bilibililevel6.net.RetrofitManager

/**
 * author：liuzipeng
 * time: 2022/11/27 00:27
 */
open class BaseRepo {
    protected val webService by lazy {
        RetrofitManager.createWebService(
            BilibiliHost.WEB_INTEFACE,
            NetService::class.java
        )
    }
}