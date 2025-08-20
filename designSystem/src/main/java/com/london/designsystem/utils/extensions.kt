package com.london.designsystem.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun @receiver:StringRes Int.string(vararg formatArgs: Any): String =
    stringResource(id = this, formatArgs = formatArgs)

val @receiver:StringRes Int.string: String
    @Composable get() = stringResource(id = this)

val @receiver:DrawableRes Int.painter: Painter
    @Composable get() = painterResource(id = this)
