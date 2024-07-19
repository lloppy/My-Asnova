package com.asnova.domain.repository.firebase

import com.asnova.model.NewsItem
import com.asnova.model.Resource
import com.asnova.model.WallItem


interface NewsRepository {
    fun addNewsItem(newsItem: NewsItem, callback: (Resource<Boolean>) -> Unit)
    fun getNewsArticlesByOrder(order: String, callback: (Resource<List<NewsItem>>) -> Unit)
    fun getNewsItemById(id: String, callback: (Resource<NewsItem>) -> Unit)

    // Get news
    fun getAsnovaNewsUseCase(callback: (Resource<List<WallItem>>) -> Unit)
    fun getSafetyNewsUseCase(callback: (Resource<List<WallItem>>) -> Unit)

    // download more news
    fun onDownloadMoreAsnovaNewsUseCase(offset: Int, callback: (Resource<List<WallItem>>) -> Unit)
    fun onDownloadMoreSafetyNewsUseCase(offset: Int, callback: (Resource<List<WallItem>>) -> Unit)
}