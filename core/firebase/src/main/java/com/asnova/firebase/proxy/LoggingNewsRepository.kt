package com.asnova.firebase.proxy

import android.util.Log
import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.model.NewsItem
import com.asnova.model.Resource
import com.asnova.model.WallItem

class LoggingNewsRepository(private val repository: NewsRepository)
    : Logger("LoggingNewsRepository"), NewsRepository {

    override fun <T> logResourceResult(methodName: String, resource: Resource<T>) {
        when (resource) {
            is Resource.Loading -> Log.d(tag, "$methodName called - Loading")
            is Resource.Success -> Log.d(tag, "$methodName result: ${resource.data}, message: ${resource.message}")
            is Resource.Error -> Log.e(tag, "$methodName error: ${resource.message}")
        }
    }

    override fun getNewsArticlesByOrder(order: String, callback: (Resource<List<NewsItem>>) -> Unit) {
        Log.d(tag, "getNewsArticlesByOrder called with order: $order")
        callback(Resource.Loading())
        repository.getNewsArticlesByOrder(order) { resource ->
            logResourceResult("getNewsArticlesByOrder", resource)
            callback(resource)
        }
    }

    override fun getNewsItemById(id: String, callback: (Resource<NewsItem>) -> Unit) {
        Log.d(tag, "getNewsItemById called with id: $id")
        callback(Resource.Loading())
        repository.getNewsItemById(id) { resource ->
            logResourceResult("getNewsItemById", resource)
            callback(resource)
        }
    }

    override fun getAsnovaNews(callback: (Resource<List<WallItem>>) -> Unit) {
        Log.d(tag, "getAsnovaNews called")
        callback(Resource.Loading())
        repository.getAsnovaNews { resource ->
            logResourceResult("getAsnovaNews", resource)
            callback(resource)
        }
    }

    override fun getSafetyNews(callback: (Resource<List<WallItem>>) -> Unit) {
        Log.d(tag, "getSafetyNews called")
        callback(Resource.Loading())
        repository.getSafetyNews { resource ->
            logResourceResult("getSafetyNews", resource)
            callback(resource)
        }
    }

    override fun onDownloadMoreAsnovaNews(offset: Int, callback: (Resource<List<WallItem>>) -> Unit) {
        Log.d(tag, "onDownloadMoreAsnovaNews called with offset: $offset")
        repository.onDownloadMoreAsnovaNews(offset) { resource ->
            logResourceResult("onDownloadMoreAsnovaNews", resource)
            callback(resource)
        }
    }

    override fun onDownloadMoreSafetyNews(offset: Int, callback: (Resource<List<WallItem>>) -> Unit) {
        Log.d(tag, "onDownloadMoreSafetyNews called with offset: $offset")
        repository.onDownloadMoreSafetyNews(offset) { resource ->
            logResourceResult("onDownloadMoreSafetyNews", resource)
            callback(resource)
        }
    }
}