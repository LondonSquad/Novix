@file:KoverIgnore
package com.london.data.mapper.recent

import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.domain.KoverIgnore

fun RecentSearchLocal.toStringQuery(): String{
    return this.query
}

fun String.toRecentSearch(): RecentSearchLocal= RecentSearchLocal(
    query = this,
    date = System.currentTimeMillis(),
)