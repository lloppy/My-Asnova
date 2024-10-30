package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.NewsFacade
import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.model.NewsItem
import com.asnova.model.Resource

// get VK News Articles
class GetNewsByOrderUseCase(
    private val newsFacade: NewsFacade
) {
    operator fun invoke(order: String, callback: (Resource<List<NewsItem>>) -> Unit) {
        return newsFacade.getAllNewsOrderedBy(order, callback)
    }
}