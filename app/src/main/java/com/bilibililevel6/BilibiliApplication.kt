package com.bilibililevel6

import android.app.Application
import com.bilibililevel6.net.RetrofitManager

/**
 * author：liuzipeng
 * time: 2022/11/14 22:50
 */
class BilibiliApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitManager.init(this)
        RetrofitManager.loadCookie(this)
    }
}