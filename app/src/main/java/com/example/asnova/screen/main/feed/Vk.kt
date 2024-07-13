package com.example.asnova.screen.main.feed

import android.util.Log
import com.example.asnova.screen.main.feed.api.VkApiError
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import okhttp3.*
import kotlinx.coroutines.*

@Serializable
data class VkResponse<R>(
    @SerialName("response") val response: R? = null,
    @SerialName("error") val error: VkApiError? = null
)

@Serializable
data class WallResponse(val items: List<WallItem2>)

@Serializable
data class WallItem2(
    val id: Int,
    val from_id: Int,
    val text: String,
    val attachments: List<Attachment>?
)

@Serializable
data class Attachment(
    val type: String,
    val photo: Photo?
)

@Serializable
data class Photo(
    val id: Int,
    val album_id: Int,
    val owner_id: Int,
    val sizes: List<PhotoSize>,
    val text: String,
    val date: Long
)

@Serializable
data class PhotoSize(
    val type: String,
    val url: String,
    val width: Int,
    val height: Int
)

class VkClient(private val accessToken: String, private val groupId: String) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    fun getGroupWall(callback: (List<WallItem2>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val request = Request.Builder()
                .url("https://api.vk.com/method/wall.get?owner_id=-$groupId&access_token=$accessToken&v=5.131")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    callback(null)
                    return@use
                }

                val responseBody = response.body?.string() ?: ""

                Log.e("vk_info", responseBody)

                val jsonElement = json.parseToJsonElement(responseBody)
                Log.e("vk_info", jsonElement.toString())

                val vkResponse = json.decodeFromJsonElement<VkResponse<WallResponse>>(jsonElement)
                withContext(Dispatchers.Main) {
                    callback(vkResponse.response?.items)
                }
            }
        }
    }
}