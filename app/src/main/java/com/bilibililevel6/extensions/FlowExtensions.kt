package com.bilibililevel6.extensions

import androidx.lifecycle.*
import com.bilibililevel6.net.BaseDataOfWeb
import com.bilibililevel6.net.NetExceptionHandler
import com.bilibililevel6.net.RequestException
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

fun <T : BaseDataOfWeb<R>, R> Flow<T>.handleResponseCode(): Flow<R> =
    transform { value ->
        if (value.code == 0) {
            return@transform emit(value.data)
        } else {
            throw RequestException(value.code, value.message)
        }
    }


fun LifecycleOwner.launchObserve(
    state: Lifecycle.State = Lifecycle.State.RESUMED,
    block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(state, block)
    }
}

inline fun <T, R> StateFlow<T>.observeState(crossinline transform: suspend (value: T) -> R): Flow<R> {
    return this.map { transform(it) }.distinctUntilChanged()
}

fun <T> MutableSharedFlow<T>.emitEvent(viewModel: ViewModel, uiEvent: T) {
    viewModel.viewModelScope.launch {
        emit(uiEvent)
    }
}