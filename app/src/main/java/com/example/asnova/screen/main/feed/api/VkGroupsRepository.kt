package com.example.asnova.screen.main.feed.api

import android.util.Log
import org.json.JSONException
import java.util.Date
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
        this.items
            .filter { it.text.isNotBlank() }
            .map { response ->
                var posterName = ""
                var posterThumbnail = ""

            this.groups.filter { abs(it.id) == abs(response.fromId) }.getOrNull(0)?.let {
                posterName = it.name
                posterThumbnail = it.photo50
            } ?: this.profiles.filter { abs(it.id) == abs(response.fromId) }.getOrNull(0)?.let {
                posterName = "${it.firstName} ${it.lastName}"
                posterThumbnail = it.photo50
            }
            val defaultImageUrl = "https://sun9-78.userapi.com/impg/Ir5UOUAUw9qczne8EVGjGw_wWvEK_Dsv_awN9Q/qguEM4hhSLA.jpg?size=1953x989&quality=96&sign=86ca45843194e357c1ea8ba559dc6117&type=album"

            WallItem(
                response.id,
                response.text,
                title = getHeadline(response.text),
                withoutTitle = removeHeadlineAndHashtags(response.text),
                posterName,
                posterThumbnail,
                date = Date(response.date * 1000L),
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
                }.ifEmpty { listOf(WallImageItem(0, 0, 0, url = defaultImageUrl))},
                hashtags = extractHashtags(response.text)
            )
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
