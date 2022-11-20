package com.bilibililevel6.extensions

import android.util.Log
import androidx.lifecycle.*
import com.bilibililevel6.net.BaseDataOfWeb
import com.bilibililevel6.net.NetExceptionHandler
import com.bilibililevel6.net.ResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * author：liuzipeng
 * time: 2022/11/18 00:26
 */
fun <T> Flow<T>.transformException(): Flow<T> {
    return catch { ex ->
        ex.printStackTrace()
        throw  NetExceptionHandler.handleException(ex)
    }
}

fun <T : BaseDataOfWeb<PopularData>, PopularData> Flow<T>.handleResponseCode(): Flow<PopularData> =
    transform { value ->
        if (value.code == 0) {
            return@transform emit(value.data)
        } else {
            throw ResponseException(value.message)
        }
    }


fun LifecycleOwner.safeObserver(
    state: Lifecycle.State = Lifecycle.State.CREATED,
    block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(state, block)
    }
}