package com.london.presentation.screen.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.presentation.utils.getValueOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<S, E : Any>(initState: S) : ViewModel() {
    private val _state = MutableStateFlow(initState)
    open val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E?>()
    open val effect = _effect.asSharedFlow()

    protected fun <T> tryToExecute(
        block: suspend () -> T,
        onStart: suspend () -> Unit = {},
        onSuccess: suspend (T) -> Unit = {},
        onError: suspend (ErrorState) -> Unit = {},
        onCompleted: suspend () -> Unit = {},
        checkSuccess: suspend (T) -> Boolean = { true },
        inScope: CoroutineScope = viewModelScope,
    ): Job {
        return inScope.launch(Dispatchers.IO) {
            runCatching {
                onStart()
                block()
            }.onSuccess { response ->
                if (checkSuccess(response)) {
                    onSuccess(response)
                    return@onSuccess
                }

                onError(ErrorState.RequestFailed().also { Timber.e(it.toString()) })
            }
                .onFailure {
                    mapExceptionToErrorState(
                        throwable = it,
                        onError = onError
                    )
                }
            onCompleted()
        }
    }

    protected fun <T> tryToCollect(
        block: suspend () -> Flow<T>,
        context: CoroutineContext = EmptyCoroutineContext,
        onStart: () -> Unit = {},
        onNewValue: suspend (T) -> Unit = {},
        onError: (ErrorState) -> Unit = {},
        onCompleted: () -> Unit = {},
        latest: Boolean = false,
        takeWhile: (T) -> Boolean = { true },
        inScope: CoroutineScope = viewModelScope,
        flowScope: Flow<T>.() -> Flow<T> = { this },
    ): Job = inScope.launch(context) {
        runCatching {
            onStart()
            val baseFlow = block()
                .run(flowScope)
                .takeWhile(takeWhile)
                .distinctUntilChanged()
                .catch { mapExceptionToErrorState(throwable = it, onError = onError) }

            if (latest)
                baseFlow.collectLatest(onNewValue)
            else
                baseFlow.collect(onNewValue)
        }
            .onFailure { mapExceptionToErrorState(throwable = it, onError = onError) }
        onCompleted()
    }

    protected fun updateState(updater: S.() -> S) {
        _state.update(updater)
    }

    protected fun emitEffect(newEffect: E) {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(newEffect)
            Timber.i("Effect -> ${newEffect::class.simpleName}")
        }
    }

    private inline fun <T> MutableStateFlow<T>.update(block: T.() -> T) {
        while (true) {
            val prevValue = value
            val nextValue = block(prevValue)
            if (compareAndSet(prevValue, nextValue))
                return
        }
    }

    private suspend fun mapExceptionToErrorState(
        throwable: Throwable,
        onError: suspend (ErrorState) -> Unit,
    ) {
        Timber.e(throwable)
        val message = throwable.message.getValueOf("message")

        val exception = when (throwable) {
            is ConnectException -> ConnectionException()
            is SocketTimeoutException,
            is TimeoutCancellationException -> TimeoutException()

            is UnknownHostException -> InternetDisconnectedException()
            else -> throwable
        }

        when (exception) {
            is UnAuthorizedException -> ErrorState.UnAuthorized
            is ConnectionException -> ErrorState.NoInternet
            is InternetDisconnectedException -> ErrorState.NoInternet
            is EmptyBodyException -> ErrorState.EmptyBody
            is TimeoutException -> ErrorState.Timeout
            is ValidationException -> ErrorState.Validation
            is ResponseException -> {
                when (exception.code) {
                    HttpStatus.SC_UNAUTHORIZED -> ErrorState.UnAuthorized
                    HttpStatus.SC_TOO_MANY_REQUESTS -> ErrorState.RequestFailed("Too many requests")
                    else -> ErrorState.RequestFailed(exception.message)
                }
            }

            else -> ErrorState.RequestFailed(message).also { Timber.e(throwable) }
        }.also { errorState ->
            Timber.e(errorState.toString())
        }.let { onError(it) }
    }
}