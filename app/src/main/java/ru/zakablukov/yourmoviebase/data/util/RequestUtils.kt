package ru.zakablukov.yourmoviebase.data.util

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

object RequestUtils {
    fun <T> requestFlow(requestFunc: suspend () -> T): Flow<Request<T>> {
        return flow<Request<T>> {
            emit(Request.Success(requestFunc()))
        }.onStart {
            emit(Request.Loading())
        }.catch { error ->
            Log.e("requestFlow", "error while loading")
            emit(Request.Error(error))
        }
    }
}