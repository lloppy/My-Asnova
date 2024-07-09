package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.NewsItem
import com.asnova.model.Resource

class GetAllFavoritesUseCase(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(callback: (Resource<List<NewsItem>>) -> Unit)
    {
        userRepository.getAllFavorites {
            when(it)
            {
                is Resource.Success -> {
                    val news: MutableList<NewsItem> = mutableListOf()
                    it.data?.forEach { id ->
                        newsRepository.getNewsItemById(id) { result ->
                            when(result)
                            {
                                is Resource.Success -> {
                                    val newsItem = result.data
                                    if(newsItem != null)
                                    {
                                        news.add(newsItem)
                                    }
                                }
                                is Resource.Error -> {
                                    callback(Resource.Error(result.message.toString()))
                                }
                                is Resource.Loading -> {
                                    callback(Resource.Loading())
                                }
                            }
                        }
                    }
                    callback(Resource.Success(news))
                }
                is Resource.Error -> {
                    callback(Resource.Error(it.message.toString()))
                }
                is Resource.Loading -> callback(Resource.Loading())
            }

        }
    }
}