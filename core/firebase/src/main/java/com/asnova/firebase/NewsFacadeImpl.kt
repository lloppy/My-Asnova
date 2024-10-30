package com.asnova.firebase

import android.net.Uri
import com.asnova.domain.repository.firebase.NewsFacade
import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.firebase.api.GroupsApi
import com.asnova.firebase.sources.NewsRepositoryImpl
import com.asnova.model.NewsItem
import com.asnova.model.Resource
import com.asnova.model.WallItem
import javax.inject.Inject

// Паттерн Facade
class NewsFacadeImpl @Inject constructor(
    groupsApi: GroupsApi
) : NewsFacade {
    // Паттерн Facade
    private val newsRepository: NewsRepository = NewsRepositoryImpl(groupsApi)

    override fun addNews(
        title: String,
        content: String,
        authors: List<String>,
        tags: List<String>,
        imageUri: Uri,
        galleryUris: List<Uri>,
        callback: (Resource<Boolean>) -> Unit
    ) {
        val newsItem = NewsItem(
            title = title,
            content = content,
            authors = authors,
            tags = tags,
            image = imageUri.toString(),
            gallery = galleryUris.map { it.toString() }
        )
        newsRepository.addNewsItem(newsItem, callback)
    }

    override fun getAllNewsOrderedBy(
        orderBy: String,
        callback: (Resource<List<NewsItem>>) -> Unit
    ) {
        newsRepository.getNewsArticlesByOrder(orderBy, callback)
    }

    override fun getNewsById(newsId: String, callback: (Resource<NewsItem>) -> Unit) {
        newsRepository.getNewsItemById(newsId, callback)
    }

    override fun fetchAsnovaNews(callback: (Resource<List<WallItem>>) -> Unit) {
        newsRepository.getAsnovaNewsUseCase(callback)
    }

    override fun fetchSafetyNews(callback: (Resource<List<WallItem>>) -> Unit) {
        newsRepository.getSafetyNewsUseCase(callback)
    }

    override fun loadMoreAsnovaNews(offset: Int, callback: (Resource<List<WallItem>>) -> Unit) {
        newsRepository.onDownloadMoreAsnovaNewsUseCase(offset, callback)
    }

    override fun loadMoreSafetyNews(offset: Int, callback: (Resource<List<WallItem>>) -> Unit) {
        newsRepository.onDownloadMoreSafetyNewsUseCase(offset, callback)
    }
}