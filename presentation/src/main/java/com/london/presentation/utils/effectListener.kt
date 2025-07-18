package com.london.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope


@Composable
fun <E> E.Listen(onEffect: suspend CoroutineScope.(currentEffect: E) -> Unit) {
    LaunchedEffect(key1 = this) {
        onEffect(this@Listen)
    }
}

@Composable
inline fun <reified E> Any?.Listen(
    noinline filter: E.() -> Boolean = { true },
    crossinline onEffect: suspend CoroutineScope.(currentEffect: E) -> Unit
) {
    if (this is E && filter(this))
        LaunchedEffect(key1 = this) { onEffect(this@Listen) }
}
