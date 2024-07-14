package com.example.asnova.screen.main.feed.api

import android.util.Log
import com.example.asnova.screen.main.feed.VkResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import javax.inject.Inject
import kotlin.math.abs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class VkGroupsRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val accessToken: String
) : GroupsRepository {
    override suspend fun getWallById(
        groupId: Int,
        offset: Int,
        count: Int,
        extended: Int?,
        fields: List<String>?
    ): List<WallItem> {
      //  val url = "https://api.vk.com/method/wall.get?owner_id=$groupId&count=$count&offset=$offset&extended=${extended ?: 0}&fields=${fields?.joinToString(",")}&access_token=$accessToken&v=5.131"
        val url = "https://api.vk.com/method/wall.get?owner_id=-221091451&access_token=2c7485642c7485642c748564202f6dcfcc22c742c7485644afaf2742c0714f09e3fa61a&v=5.131"

        Log.d("vk_info", "URL: $url")

        val request = Request.Builder()
            .url(url)
            .build()

        val response = withContext(Dispatchers.IO) {
            okHttpClient.newCall(request).execute()
        }

        Log.d("vk_info", "Response received")

        if (!response.isSuccessful) {
            Log.e("vk_info", "Request failed: ${response.code}, ${response.message}")
            throw VkException(response.code, response.message)
        }

        val responseBody = response.body?.string() ?: throw JSONException("Empty response body")

        Log.d("vk_info", "Response body: $responseBody")

        val vkResponse = Json.decodeFromString<VkResponse<GroupWallResponse>>(responseBody)

        Log.d("vk_info", "VK Response parsed, items size: ${vkResponse.response?.items?.size}")

        return getResponseOrThrow(vkResponse).mapToDomain()
    }

    private fun <R : Any> getResponseOrThrow(response: VkResponse<R>): R {
        when {
            response.error != null -> {
                Log.e("vk_info", "VK API Error: ${response.error.errorCode}, ${response.error.errorMsg}")
                throw VkException(response.error.errorCode, response.error.errorMsg)
            }
            response.response != null -> return response.response
            else -> throw JSONException("Unknown JSON")
        }
    }

    fun GroupWallResponse.mapToDomain(): List<WallItem> =
        this.items.map { response ->
            var posterName = ""
            var posterThumbnail = ""

            this.groups.filter { abs(it.id) == abs(response.fromId) }.getOrNull(0)?.let {
                posterName = it.name
                posterThumbnail = it.photo50
            } ?: this.profiles.filter { abs(it.id) == abs(response.fromId) }.getOrNull(0)?.let {
                posterName = "${it.firstName} ${it.lastName}"
                posterThumbnail = it.photo50
            }
            WallItem(
                response.id,
                response.text,
                posterName,
                posterThumbnail,
                response.date,
                response.likes.userLikes,
                response.likes.count,
                response.attachments.filter { it.type == "photo" }.map {
                    WallImageItem(
                        it.photo.id,
                        it.photo.sizes.filter { resized -> resized.type == "r" }
                            .getOrNull(0)?.height ?: 0,
                        it.photo.sizes.filter { resized -> resized.type == "r" }
                            .getOrNull(0)?.width ?: 0,
                        it.photo.sizes.filter { resized -> resized.type == "r" }
                            .getOrNull(0)?.url ?: ""
                    )
                }
            )
        }
}
