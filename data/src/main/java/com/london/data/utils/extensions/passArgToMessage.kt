package com.london.data.utils.extensions

import com.london.data.datasource.local.BaseException


fun BaseException.passArgToMessage(arg: Any) = apply { message.format(arg::class.java.name) }
