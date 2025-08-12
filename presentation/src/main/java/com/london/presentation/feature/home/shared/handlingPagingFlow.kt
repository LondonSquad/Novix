package com.london.presentation.feature.home.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.PagedFetchResponse
import com.london.presentation.shared.base.createPagingSourceFlow
import kotlinx.coroutines.flow.Flow

fun <T : Any> ViewModel.handlingPagingFlow(
    query: String? = null,
    fetchPage: suspend (page: Int) -> PagedFetchResponse<T>
): Flow<PagingData<T>> {
    return createPagingSourceFlow(
        query = query.orEmpty(),
        block = { _, pageNumber -> fetchPage(pageNumber) }
    ).cachedIn(viewModelScope)
}
