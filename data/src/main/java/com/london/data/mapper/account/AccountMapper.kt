package com.london.data.mapper.account

import com.london.data.remote.model.account.AccountInfoResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.account.AccountInfo

fun AccountInfoResponse.toEntity(): AccountInfo = AccountInfo(
    id = this.id.orZero(),
    userName = this.name?.takeIf { it.isNotBlank() } ?: this.userName.orEmpty(),
    avatarPath = this.avatar?.tmdb?.avatarPath.asImageUrlOrEmpty()
)

