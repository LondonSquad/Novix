package com.london.data.mapper.account

import com.london.data.remote.model.account.AccountInfoResponse
import com.london.data.utils.orZero
import com.london.domain.entity.AccountInfo

fun AccountInfoResponse.toEntity(): AccountInfo {
    return AccountInfo(
        id = this.id.orZero(),
        username = this.userName.orEmpty()
    )
}
