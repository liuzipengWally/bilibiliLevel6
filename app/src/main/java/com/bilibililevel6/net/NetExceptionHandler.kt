package com.bilibililevel6.net

import android.net.ParseException
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


/**
 * Created by liuzipeng on 2017/2/20.
 */
class NetExceptionHandler {
    companion object {
        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504

        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val NETWORK_ERROR = 1002

        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005

        /**
         * host错误
         */
        const val HOST_ERROR = 1006

        /**
         * 连接超时
         */
        const val TIMEOUT = 1007

        /**
         * 服务器 响应提 异常
         */
        const val SERVER_ERROR = 1008

        /**
         * 响应体异常的默认code，防止有些响应体可能没有code
         */
        const val RESPONSE_ERROR_DEF_CODE = 0

        fun handleException(e: Throwable): Throwable {
            when (e) {
                is RequestException -> return e
                is ResponseException -> return e
                is HttpException -> {
                    val msg = when (e.code()) {
                        UNAUTHORIZED -> "我们的访问被服务器拒绝啦~(${e.code()})"
                        FORBIDDEN -> "服务器资源不可用(${e.code()})"
                        NOT_FOUND -> "我们好像迷路了，找不到服务器(${e.code()})"
                        REQUEST_TIMEOUT -> "糟糕，我们的请求超时了，请检查网络连接后重试(${e.code()})"
                        GATEWAY_TIMEOUT -> "糟糕，我们的请求超时了，请检查网络连接后重试(${e.code()})"
                        INTERNAL_SERVER_ERROR -> "服务器正在开小差，请稍后重试(${e.code()})"
                        BAD_GATEWAY -> "服务器正在开小差，请稍后重试(${e.code()})"
                        SERVICE_UNAVAILABLE -> "服务器可能正在维护，请稍后重试(${e.code()})"
                        else -> "网络异常，请检查网络连接后重试(${e.code()})"
                    }
                    return RequestException(msg)
                }
                is JsonParseException, is JSONException, is ParseException -> {
                    return ResponseException("数据解析错误，这可能是一个bug(${PARSE_ERROR})")
                }
                is ConnectException -> {
                    return RequestException("连接失败，网络连接可能存在异常，请检查网络后重试(${NETWORK_ERROR})")
                }
                is SSLHandshakeException -> {
                    return RequestException("证书验证失败(${SSL_ERROR})")
                }
                is UnknownHostException -> {
                    return RequestException("无法连接到服务器，请检查你的网络或稍后重试(${HOST_ERROR})")
                }
                is SocketTimeoutException -> {
                    return RequestException("连接超时,请稍候重试(${TIMEOUT})")
                }
                else -> {
                    return RequestException("出现了未知的错误~(${UNKNOWN})")
                }
            }
        }
    }
}

class ResponseException(msg: String) : Throwable(msg)

class RequestException(msg: String) : Throwable(msg)