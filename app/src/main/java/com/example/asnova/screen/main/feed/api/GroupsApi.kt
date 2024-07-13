package com.example.asnova.screen.main.feed.api

import com.example.asnova.screen.main.feed.VkResponse
import retrofit2.http.*

interface GroupsApi {
    // https://dev.vk.com/ru/method/wall
    @GET("wall.get")
    suspend fun getWallById(
        @Query("v") version: String,
        @Query("owner_id") groupId: Int,
        @Query("offset") offset: Int,
        @Query("count") count: Int,
        @Query("extended") extended: Int?,
        @Query("fields") fields: String?
    ): VkResponse<GroupWallResponse>
}