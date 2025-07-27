package com.london.presentation.utils

import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


fun <T : Any> Flow<PagingData<T>>.update(transform: (T) -> T): Flow<PagingData<T>> =
    map { pagingData -> pagingData.map(transform = transform) }

@JvmName("updateAny")
fun <T : Any, R : Any> Flow<PagingData<T>>.update(transform: (T) -> R): Flow<PagingData<R>> =
    map { pagingData -> pagingData.map(transform = transform) }

fun <T : Any> LazyPagingItems<T>.isNotEmpty(): Boolean = itemSnapshotList.isNotEmpty()
fun <T : Any> LazyPagingItems<T>.isEmpty(): Boolean = itemSnapshotList.isEmpty()

fun <T : Any> LazyPagingItems<T>.getOrNull(index: Int): T? = runCatching { this[index] }.getOrNull()

fun <T : Any> LazyPagingItems<T>.isLoading(): Boolean = loadState.refresh is LoadState.Loading
fun <T : Any> LazyPagingItems<T>.isAppendLoading(): Boolean = loadState.append is LoadState.Loading

fun <T : Any> LazyPagingItems<T>.isError(): Boolean = loadState.refresh is LoadState.Error
fun <T : Any> LazyPagingItems<T>.isAppendError(): Boolean = loadState.append is LoadState.Error