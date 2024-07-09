package com.asnova.domain.repository.firebase

import com.asnova.model.NewsItem
import com.asnova.model.Resource

interface NewsRepository {
    fun addNewsItem(newsItem: NewsItem, callback: (Resource<Boolean>) -> Unit)
    fun getNewsArticlesByOrder(order: String, callback: (Resource<List<NewsItem>>) -> Unit)
    fun getNewsItemById(id: String, callback: (Resource<NewsItem>) -> Unit)
    fun deleteNewsItemById(id: String, callback: (Resource<Boolean>) -> Unit)
    fun getVKNewsArticles(callback: (Resource<List<NewsItem>>) -> Unit)
}