package com.asnova.firebase

import android.net.Uri
import android.util.Log
import com.asnova.domain.repository.firebase.NewsRepository
import com.asnova.model.NewsItem
import com.asnova.model.Resource
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
import org.jsoup.Jsoup
import java.util.UUID
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor() : NewsRepository {
    // https://dev.vk.com/ru/method/groups
    private val _database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _databaseReference: CollectionReference = _database.collection("news")
    private val _storage: FirebaseStorage = Firebase.storage
    private val _storageReference: StorageReference = _storage.reference

    private val newsUrl = "https://asnova.pro"
    // asnova  "https://oauth.vk.com/authorize?" + "client_id=51985947" + "&redirect_uri=https://oauth.vk.com/blank.html" + "&group_ids=162375388" + "&display=page" + "&scope=wall,photos,groups" + "&response_type=token" + "&v=5.131"
    // chineese  "https://oauth.vk.com/authorize?" + "client_id=51985947" + "&redirect_uri=https://oauth.vk.com/blank.html" + "&group_ids=221091451" + "&display=page" + "&scope=wall,photos,groups" + "&response_type=token" + "&v=5.131"
    // asnova https://oauth.vk.com/authorize?client_id=51985947&group_ids=162375388&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=wall,groups&response_type=token&v=5.131
    // chineese https://oauth.vk.com/authorize?client_id=51985947&group_ids=221091451&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=wall,groups&response_type=token&v=5.131

    // vk1.a.kqcfhxbPlKsLzyIqoNzSSmonCRSyg5RlSeypPry1_JQTOqGlCALd_ySQzKnZP1foLfhgQThN4dZ0LOChaPmr1CVY6419FJM5x71u0lwgw8b4hW2Gd1IwM65b7miAhnMqrKlp-3CRAvtHNruhtosDVR6aHc1zBvwOz2HlPIlu00nfxiBBxyGVWbEwPUccuiyl47OiLlGHFcrrwJc7sRKliQ

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

    override fun getVKNewsArticles(callback: (Resource<List<NewsItem>>) -> Unit) {
        callback(Resource.Loading())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val doc = Jsoup.connect(newsUrl).get()

                Log.e("newsTag", doc.title())
                Log.e("newsTag", doc.data())

                val newsItems = mutableListOf<NewsItem>()
                val newsItem = NewsItem(
                    image = "https://asnova.pro/thumb/2/5DD6qxOMlnoQps2Rxbj3wA/400r/d/l.jpg",
                    title = doc.title(),
                    published = parseDate(),
                    content = doc.title(),
                    authors = listOf("Author Name"),
                    gallery = emptyList(),
                    tags = listOf("Tag1", "Tag2"),
                    id = "124"
                )

                newsItems.add(newsItem)

                withContext(Dispatchers.Main) {
                    callback(Resource.Success(newsItems))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(Resource.Error("Failed to fetch news: ${e.message}"))
                }
            }
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
            callback(Resource.Error("Something happened on the server side")) // TODO: Must be return ErrorState
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
                callback(Resource.Error("This article was not found")) // TODO: Must be return ErrorState
            }
        }.addOnFailureListener {
            callback(Resource.Error(it.message.toString())) // TODO: Must be return ErrorState
        }
    }

    override fun deleteNewsItemById(id: String, callback: (Resource<Boolean>) -> Unit) {
        TODO("Not yet implemented")
    }

    private fun parseDate(dateString: String): Long {
        // TODO: Implement date parsing logic
        return System.currentTimeMillis()
    }

    private fun parseDate(): Long {
        return System.currentTimeMillis()
    }
}
