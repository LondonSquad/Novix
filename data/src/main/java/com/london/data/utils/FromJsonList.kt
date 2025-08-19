package com.london.data.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.jsonList(json: String): List<T> =
    fromJson(json, object : TypeToken<List<T>>() {}.type)
