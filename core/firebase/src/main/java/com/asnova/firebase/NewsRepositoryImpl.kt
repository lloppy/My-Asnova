package com.asnova.firebase

import android.net.Uri
import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.firebase.api.GroupsApi
import com.asnova.model.NewsItem
import com.asnova.model.Resource
import com.asnova.model.WallImageItem
import com.asnova.model.WallItem
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID
import javax.inject.Inject

const val DEFAULT_IMAGE_RESOURCE_URL = "resource://ic_asnova_default_news"

class NewsRepositoryImpl @Inject constructor(
    private val groupsApi: GroupsApi

) : NewsRepository {
    // https://dev.vk.com/ru/method/groups
    private val accessToken = "2c7485642c7485642c748564202f6dcfcc22c742c7485644afaf2742c0714f09e3fa61a"

    private val _database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _databaseReference: CollectionReference = _database.collection("news")
    private val _storage: FirebaseStorage = Firebase.storage
    private val _storageReference: StorageReference = _storage.reference

    override fun addNewsItem(newsItem: NewsItem, callback: (Resource<Boolean>) -> Unit) {
        callback(Resource.Loading())
        val id = _databaseReference.document().id
        val uuid = UUID.randomUUID()
        var imageUri = ""
        val gallery: MutableList<String> = mutableListOf()

        val tasksList = mutableListOf<Task<Uri>>()

        try {
            newsItem.gallery.forEach { string ->
                val temp = UUID.randomUUID()
                val imageReference = _storageReference.child("images/${id}/gallery/${temp}")
                val uploadTask = imageReference.putFile(Uri.parse(string))

                tasksList.add(uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    return@continueWithTask imageReference.downloadUrl
                })
            }

            Tasks.whenAllSuccess<Uri>(tasksList).addOnSuccessListener {
                it.forEach { uri ->
                    gallery.add(uri.toString())
                }
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    _storageReference.child("images/${id}/${uuid}")
                        .putFile(Uri.parse(newsItem.image)).addOnSuccessListener {
                            _storageReference.child("images/${id}/${uuid}").downloadUrl.addOnSuccessListener { uri ->
                                imageUri = uri.toString()
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val article = com.asnova.firebase.model.NewsItem(
                                        image = imageUri,
                                        title = newsItem.title,
                                        published = Timestamp.now(),
                                        content = newsItem.content,
                                        authors = newsItem.authors,
                                        tags = newsItem.tags,
                                        gallery = gallery,
                                        id = id
                                    )
                                    _databaseReference.document(article.id).set(article)
                                        .addOnCompleteListener { task2 ->
                                            if (task2.isSuccessful) {
                                                callback(Resource.Success(data = true))
                                            } else
                                                callback(Resource.Success(data = false))
                                        }.addOnFailureListener { exception ->
                                            callback(Resource.Error(exception.message.toString()))
                                        }
                                }
                            }
                        }
                }
            }
        } catch (e: FirebaseException) {
            callback(Resource.Error(e.message.toString()))
        }
    }

    override fun getNewsArticlesByOrder(
        order: String,
        callback: (Resource<List<NewsItem>>) -> Unit
    ) {
        callback(Resource.Loading())
        _databaseReference.orderBy(order, Query.Direction.DESCENDING).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val list: MutableList<NewsItem> = mutableListOf()
                    snapshot.documents.forEach { document ->
                        val newsItem =
                            document.toObject(com.asnova.firebase.model.NewsItem::class.java)
                        if (newsItem != null) {
                            list.add(
                                NewsItem(
                                    image = newsItem.image,
                                    title = newsItem.title,
                                    published = newsItem.published.seconds,
                                    content = newsItem.content,
                                    authors = newsItem.authors,
                                    gallery = newsItem.gallery,
                                    tags = newsItem.tags,
                                    id = newsItem.id
                                )
                            )
                        }
                    }
                    callback(Resource.Success(list))
                } else
                    callback(Resource.Error("No news")) // TODO: Must be return ErrorState
            }.addOnFailureListener {
                callback(Resource.Error("Ошибка на стороне сервера, проверьте интернет-подключение")) // TODO: Must be return ErrorState
            }
    }

    override fun getNewsItemById(id: String, callback: (Resource<NewsItem>) -> Unit) {
        callback(Resource.Loading())
        _databaseReference.document(id).get().addOnSuccessListener { snapshot ->
            if (snapshot.data != null) {
                val temp = snapshot.toObject(com.asnova.firebase.model.NewsItem::class.java)
                if (temp != null) {
                    val newsItem = NewsItem(
                        image = temp.image,
                        title = temp.title,
                        published = temp.published.seconds,
                        content = temp.content,
                        authors = temp.authors,
                        gallery = temp.gallery,
                        tags = temp.tags,
                        id = temp.id
                    )
                    callback(Resource.Success(newsItem))
                }
            } else {
                callback(Resource.Error("This article was not found"))
            }
        }.addOnFailureListener {
            callback(Resource.Error(it.message.toString()))
        }
    }

    override fun getAsnovaNewsUseCase(callback: (Resource<List<WallItem>>) -> Unit) {
        fetchWallNews(
            ownerId = -162375388,
            callback = callback,
            baseUrl = "asnovapro"
        )
    }

    override fun getSafetyNewsUseCase(callback: (Resource<List<WallItem>>) -> Unit) {
        fetchWallNews(
            ownerId = -80108699,
            callback = callback,
            baseUrl = "asnova1005"
        )
    }

    override fun onDownloadMoreAsnovaNewsUseCase(offset: Int, callback: (Resource<List<WallItem>>) -> Unit) {
        fetchWallNews(
            ownerId = -162375388,
            offset = offset,
            callback = callback,
            baseUrl = "asnovapro"
        )
    }

    override fun onDownloadMoreSafetyNewsUseCase(offset: Int, callback: (Resource<List<WallItem>>) -> Unit) {
        fetchWallNews(
            ownerId = -80108699,
            offset = offset,
            callback = callback,
            baseUrl = "asnova1005"
        )
    }

    private fun fetchWallNews(
        ownerId: Int,
        offset: Int = 0,
        callback: (Resource<List<WallItem>>) -> Unit,
        baseUrl: String
    ) {
        callback(Resource.Loading())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val version = "5.131"
                val count = 10
                val extended = 1

                val wallResponse = groupsApi.getWall(
                    version = version,
                    ownerId = ownerId,
                    accessToken = accessToken,
                    count = count,
                    offset = offset,
                    extended = extended,
                    fields = null
                )

                withContext(Dispatchers.Main) {
                    val wallItems = wallResponse.response?.items?.filter { it.text.isNotBlank() }?.map { response ->
                        WallItem(
                            id = response.id,
                            text = response.text,
                            title = getHeadline(response.text),
                            withoutTitle = removeHeadlineAndHashtags(response.text),
                            date = Date(response.date * 1000L),
                            userLikes = response.likes.userLikes,
                            likesCount = response.likes.count,
                            images = response.attachments.filter { it.type == "photo" }.map {
                                WallImageItem(
                                    id = it.photo.id,
                                    height = it.photo.sizes.filter { resized -> resized.type == "r" }.getOrNull(0)?.height ?: 0,
                                    width = it.photo.sizes.filter { resized -> resized.type == "r" }.getOrNull(0)?.width ?: 0,
                                    url = it.photo.sizes.filter { resized -> resized.type == "r" }.getOrNull(0)?.url ?: DEFAULT_IMAGE_RESOURCE_URL
                                )
                            }.ifEmpty {
                                listOf(WallImageItem(0, 0, 0, DEFAULT_IMAGE_RESOURCE_URL))
                            },
                            hashtags = extractHashtags(response.text),
                            postUrl = "https://vk.com/$baseUrl?w=wall${response.ownerId}_${response.id}"
                        )
                    } ?: emptyList()

                    callback(Resource.Success(wallItems))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(Resource.Error("Something happened on the server side"))
                }
            }
        }
    }
}

private fun getHeadline(messageText: String): String {
    val regex = Regex(".*?[.!?\\s](?=\\p{Punct}|\\p{So}|\\p{Sc}|\\s|\\z)")
    val match = regex.find(messageText)
    return match?.value ?: messageText
}

private fun extractHashtags(text: String): List<String> {
    val hashtagPattern = "#(?!_lp_block)\\w+".toRegex()
    return hashtagPattern.findAll(text)
        .map { it.value }
        .toList()
}

private fun removeHeadlineAndHashtags(text: String): String {
    val headline = getHeadline(text)
    val textWithoutHeadline = text.removePrefix(headline).trim()

    val hashtagPattern = "#\\w+".toRegex()
    val textWithoutHashtags = hashtagPattern.replace(textWithoutHeadline, "").trim()

    return textWithoutHashtags
}
