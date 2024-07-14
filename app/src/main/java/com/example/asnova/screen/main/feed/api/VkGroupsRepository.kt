package com.example.asnova.screen.main.feed.api

import android.util.Log
import com.example.asnova.screen.main.feed.VkResponse
import org.json.JSONException
import javax.inject.Inject
import kotlin.math.abs

class VkGroupsRepository @Inject constructor(
    private val fileUtils: FileUtils,
    private val groupsApi: GroupsApi

) : GroupsRepository {
    private val accessToken = "2c7485642c7485642c748564202f6dcfcc22c742c7485644afaf2742c0714f09e3fa61a"
    override suspend fun getWallById(
        groupId: Int,
        offset: Int,
        count: Int,
        extended: Int?,
        fields: List<String>?
    ): List<WallItem> {
        val url = "https://api.vk.com/method/wall.get?" +
                "owner_id=$groupId&" +
                "count=$count&" +
                "offset=$offset&" +
                "extended=${extended ?: 0}&" +
                "fields=${fields?.joinToString(",")}&" +
                "access_token=$accessToken&" +
                "v=5.131"
        Log.d("vk_info", "Request URL: $url")

        val wall = groupsApi.getWall(
            version = "5.131",
            ownerId = groupId,
            accessToken = accessToken,
            offset = offset,
            count = count,
            extended = extended,
            fields = fields?.joinToString(",")
        )

        Log.d("vk_info", "Response received: $wall")

        return getResponseOrThrow(wall).mapToDomain()
    }

    private fun <R : Any> getResponseOrThrow(response: VkResponse<R>): R {
        when {
            response.error != null -> throw VkException(
                response.error.errorCode,
                response.error.errorMsg
            )

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
