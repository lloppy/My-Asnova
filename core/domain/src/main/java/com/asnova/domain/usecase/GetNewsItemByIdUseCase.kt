package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.model.NewsItem
import com.asnova.model.Resource

class GetNewsItemByIdUseCase(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(id: String, callback: (Resource<NewsItem>) -> Unit) {
        return newsRepository.getNewsItemById(id, callback)
    }
}