package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.model.Resource
import com.asnova.model.WallItem

// Паттерн Facade
// один из примеров использования
class GetAsnovaNewsUseCase(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(callback: (Resource<List<WallItem>>) -> Unit)
    {
        return newsRepository.getAsnovaNews(callback)
    }
}