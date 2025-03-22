package com.example.caseapp.network

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is Exception -> NetworkResult.Error(throwable)
            else -> NetworkResult.Error(Exception(throwable))
        }
    }
}

fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) {
        action(data)
    }
    return this
}

fun <T> NetworkResult<T>.onError(action: (Exception) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) {
        action(exception)
    }
    return this
}

fun <T> NetworkResult<T>.onLoading(action: () -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Loading) {
        action()
    }
    return this
}

fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(data))
        is NetworkResult.Error -> NetworkResult.Error(exception)
        is NetworkResult.Loading -> NetworkResult.Loading
    }
}

suspend fun <T, R> NetworkResult<T>.suspendMap(transform: suspend (T) -> R): NetworkResult<R> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.Success(transform(data))
        is NetworkResult.Error -> NetworkResult.Error(exception)
        is NetworkResult.Loading -> NetworkResult.Loading
    }
}

fun <T> NetworkResult<T>.getOrNull(): T? {
    return when (this) {
        is NetworkResult.Success -> data
        else -> null
    }
}

fun <T> NetworkResult<T>.getOrDefault(defaultValue: T): T {
    return when (this) {
        is NetworkResult.Success -> data
        else -> defaultValue
    }
}

fun <T> NetworkResult<T>.getOrThrow(): T {
    return when (this) {
        is NetworkResult.Success -> data
        is NetworkResult.Error -> throw exception
        is NetworkResult.Loading -> throw IllegalStateException("Result is Loading")
    }
}