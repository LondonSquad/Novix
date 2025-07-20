package com.london.presentation.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import kotlin.reflect.KType


inline fun <reified T : Any> SavedStateHandle.getArgs(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap()
): T? = runCatching { toRoute<T>(typeMap = typeMap) }.getOrNull()