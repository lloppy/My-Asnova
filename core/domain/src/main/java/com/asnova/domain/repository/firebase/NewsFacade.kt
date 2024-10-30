package com.asnova.domain.repository.firebase


import android.net.Uri
import com.asnova.model.NewsItem
import com.asnova.model.Resource
import com.asnova.model.WallItem

// Паттерн Facade
interface NewsFacade {
    fun addNews(
        title: String,
        content: String,
        authors: List<String>,
        tags: List<String>,
        imageUri: Uri,
        galleryUris: List<Uri>,
        callback: (Resource<Boolean>) -> Unit
    )

    fun getAllNewsOrderedBy(orderBy: String, callback: (Resource<List<NewsItem>>) -> Unit)

    fun getNewsById(newsId: String, callback: (Resource<NewsItem>) -> Unit)

    fun fetchAsnovaNews(callback: (Resource<List<WallItem>>) -> Unit)

    fun fetchSafetyNews(callback: (Resource<List<WallItem>>) -> Unit)

    fun loadMoreAsnovaNews(offset: Int, callback: (Resource<List<WallItem>>) -> Unit)

    fun loadMoreSafetyNews(offset: Int, callback: (Resource<List<WallItem>>) -> Unit)
}