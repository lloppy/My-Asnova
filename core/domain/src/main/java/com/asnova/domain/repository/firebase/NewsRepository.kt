package com.asnova.domain.repository.firebase

import com.asnova.model.NewsItem
import com.asnova.model.Resource
import com.asnova.model.WallItem


interface NewsRepository {
    fun getNewsArticlesByOrder(order: String, callback: (Resource<List<NewsItem>>) -> Unit)
    fun getNewsItemById(id: String, callback: (Resource<NewsItem>) -> Unit)

    // Get news
    fun getAsnovaNews(callback: (Resource<List<WallItem>>) -> Unit)
    fun getSafetyNews(callback: (Resource<List<WallItem>>) -> Unit)

    // download more news
    fun onDownloadMoreAsnovaNews(offset: Int, callback: (Resource<List<WallItem>>) -> Unit)
    fun onDownloadMoreSafetyNews(offset: Int, callback: (Resource<List<WallItem>>) -> Unit)
}