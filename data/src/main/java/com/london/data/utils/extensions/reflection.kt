package com.london.data.utils.extensions

import kotlin.reflect.KClass
import kotlin.reflect.KParameter


/**
 *  check this gists
 *  https://gist.github.com/Al-Taie/f33f6ca3c9ba074404cd8b296e3c63a1
 */
fun Exception.passArgToMessage(
    vararg args: Any,
) = this::class.createInstance(args::class.java.name) { it.matches(String::class) }

fun List<KParameter>.matches(vararg args: KClass<*>): Boolean =
    size == args.size && zip(args).all { (parameter, argument) ->
        parameter.type.classifier == argument
    }

fun <T : Any> KClass<T>.createInstance(
    vararg args: Any,
    predicate: (List<KParameter>) -> Boolean = { true }
): T = constructors.first { predicate(it.parameters) }.call(*args)
