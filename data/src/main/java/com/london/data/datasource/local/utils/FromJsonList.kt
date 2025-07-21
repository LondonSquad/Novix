package com.london.data.datasource.local.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJsonList(json: String): List<T> =
    fromJson(json, object : TypeToken<List<T>>() {}.type)

inline fun <reified T> Gson.fromJsonObject(json: String): T =
    fromJson(json, T::class.java)
