package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.NewsFacade
import com.asnova.model.Resource
import com.asnova.model.WallItem

// Паттерн Facade
// один из примеров использования
class GetAsnovaNewsUseCase(
    private val newsFacade: NewsFacade
) {
    operator fun invoke(callback: (Resource<List<WallItem>>) -> Unit)
    {
        return newsFacade.fetchAsnovaNews(callback)
    }
}