package com.bilibililevel6.net

import android.app.Application
import android.content.Context
import com.bilibililevel6.extensions.isNetworkAvailable
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                chain.proceed(
                    request
                        .newBuilder()
//                        .url(request.url.newBuilder().addQueryParameter("","").build()) 添加默认参数
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .build()
                )
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
//                        .url(request.url.newBuilder().addQueryParameter("","").build()) 添加默认参数
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .build()
                )
            })
            .addInterceptor(getCacheInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .retryOnConnectionFailure(true).build()
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