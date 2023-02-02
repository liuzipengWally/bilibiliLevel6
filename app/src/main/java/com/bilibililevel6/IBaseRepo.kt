package com.bilibililevel6

import com.bilibililevel6.net.BilibiliHost
import com.bilibililevel6.net.NetService
import com.bilibililevel6.net.RetrofitManager

/**
 * author：liuzipeng
 * time: 2022/11/27 00:27
 */
abstract class BaseRepo {
    protected val webService by lazy {
        RetrofitManager.createWebService(
            BilibiliHost.WEB_INTERFACE,
            NetService::class.java
        )
    }

    protected val accountService by lazy {
        RetrofitManager.createWebService(
            BilibiliHost.ACCOUNT_INTERFACE,
            NetService::class.java
        )
    }
}