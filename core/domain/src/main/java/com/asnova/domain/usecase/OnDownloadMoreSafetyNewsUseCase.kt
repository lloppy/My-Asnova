package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.NewsFacade
import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.model.Resource
import com.asnova.model.WallItem

class OnDownloadMoreSafetyNewsUseCase (
    private val newsFacade: NewsFacade
) {
    operator fun invoke(offset: Int, callback: (Resource<List<WallItem>>) -> Unit) {
        return newsFacade.loadMoreSafetyNews(offset, callback)
    }
}