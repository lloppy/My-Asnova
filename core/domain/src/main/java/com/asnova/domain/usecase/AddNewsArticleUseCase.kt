package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.model.NewsItem
import com.asnova.model.Resource

class AddNewsArticleUseCase(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(newsItem: NewsItem, callback: (Resource<Boolean>) -> Unit) {
        return newsRepository.addNewsItem(newsItem, callback)
    }
}