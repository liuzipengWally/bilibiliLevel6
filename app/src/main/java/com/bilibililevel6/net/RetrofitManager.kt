package com.bilibililevel6.net

import android.app.Application
import android.content.Context
import com.bilibililevel6.Config
import com.bilibililevel6.extensions.dataStore
import com.bilibililevel6.extensions.isNetworkAvailable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.ConnectException
import java.util.concurrent.TimeUnit

/**
 * author：刘自鹏
 * email：liuzipeng@meituan.com
 * time: 2017/4/25 14:54
 * 该类用于构造Retrofit 以及生成NetService
 */
object RetrofitManager {
    private var httpClient: OkHttpClient? = null
    private var webHttpClient: OkHttpClient? = null
    private const val TIMEOUT: Long = 10

    fun init(context: Application) {
        if (httpClient == null) {
            httpClient = constructClient(context)
        }
        if (webHttpClient == null) {
            webHttpClient = constructWebHttpClient(context)
        }
    }

    fun <T> createClientService(
        baseUrl: String,
        serviceClass: Class<T>
    ): T {
        if (httpClient == null) {
            throw RuntimeException("httpClient cannot init Exception")
        }
        return Retrofit.Builder()
            .client(httpClient!!)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(serviceClass)
    }

    fun <T> createWebService(
        baseUrl: String,
        serviceClass: Class<T>
    ): T {
        if (httpClient == null) {
            throw RuntimeException("webHttpClient cannot init Exception")
        }
        return Retrofit.Builder()
            .client(webHttpClient!!)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(serviceClass)
    }

    private fun constructWebHttpClient(context: Context): OkHttpClient {
        val cacheSize = (100 * 1024 * 1024).toLong()
        //todo:cache要用唯一标识区分，可用登录后的ID
        val file = File(context.externalCacheDir, "cache")
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .cache(Cache(file, cacheSize))
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    if (Config.cookies.isEmpty()) {
                        loadCookie(context)
                    }
                    return Config.cookies
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    Config.cookies = cookies
                    saveLoggedCookie(context, Gson().toJson(cookies))
                }
            })
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(
                    request
                        .newBuilder()
                        .build()
                )
                response
            })
            .addInterceptor(getCacheInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .retryOnConnectionFailure(true).build()
    }

    private fun constructClient(context: Context): OkHttpClient {
        val cacheSize = (100 * 1024 * 1024).toLong()
        //todo:cache要用唯一标识区分，可用登录后的ID
        val file = File(context.externalCacheDir, "cache")
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .cache(Cache(file, cacheSize))
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                chain.proceed(
                    request
                        .newBuilder()
                        .build()
                )
            })
            .addInterceptor(getCacheInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .retryOnConnectionFailure(true).build()
    }

    fun loadCookie(context: Context) {
        MainScope().launch(context = Dispatchers.IO) {
            val cookie = context.dataStore.data.first().loggedCookie
            if (cookie.isNullOrEmpty()) return@launch
            val cookies: List<Cookie> =
                Gson().fromJson(cookie, object : TypeToken<List<Cookie>>() {}.type)
            Config.cookies = cookies
        }
    }

    private fun saveLoggedCookie(context: Context, realLoggedCookie: String) {
        MainScope().launch {
            context.dataStore.updateData { it.copy(loggedCookie = realLoggedCookie) }
        }
    }

    /**
     * 用于设置缓存的拦截器
     * @param context
     * @return
     */
    private fun getCacheInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            if (context.isNetworkAvailable) {
                val response = chain.proceed(request)
                val maxStale = 60 * 60 * 24 * 28 //缓存30天
                response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached,max-stale=$maxStale")
                    .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
            } else {
                val newRequest = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
                val response = chain.proceed(newRequest)
                if (response.isSuccessful) {
                    response
                } else {
                    throw ConnectException()
                }
            }
        }
    }
}