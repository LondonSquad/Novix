package com.london.data.utils.extensions

import com.london.data.datasource.local.BaseException


fun BaseException.passArgToMessage(vararg args: Any) = apply { message.format(*args) }
