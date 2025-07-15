package com.london.data.mapper.recent

import com.london.data.datasource.local.model.RecentSearch

fun RecentSearch.toStringQuery(): String{
    return this.query
}

fun String.toRecentSearch(): RecentSearch= RecentSearch(
    query = this,
    date = System.currentTimeMillis(),
)