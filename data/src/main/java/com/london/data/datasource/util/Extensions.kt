package com.london.data.datasource.util

import java.security.MessageDigest

fun String.generateHash(): String =
    MessageDigest.getInstance("MD5").digest(toByteArray()).joinToString("") { "%02x".format(it) }
