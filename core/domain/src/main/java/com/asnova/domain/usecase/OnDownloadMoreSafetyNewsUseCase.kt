package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.model.Resource
import com.asnova.model.WallItem

class OnDownloadMoreSafetyNewsUseCase (
    private val newsRepository: NewsRepository
) {
    operator fun invoke(offset: Int, callback: (Resource<List<WallItem>>) -> Unit) {
        return newsRepository.onDownloadMoreSafetyNewsUseCase(offset, callback)
    }
}