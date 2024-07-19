package com.asnova.firebase.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GroupsApi {
    // https://dev.vk.com/ru/method/wall

    /**
     * Получение записей со стены сообщества.
     *
     * @param version Версия API VK.
     * @param ownerId ID сообщества (отрицательное значение).
     * @param accessToken Токен доступа к API VK.
     * @param count Количество возвращаемых записей.
     * @param offset Смещение относительно начала списка записей.
     * @param extended 1 — возвращать дополнительные поля profiles и groups, 0 — не возвращать.
     * @param fields Список дополнительных полей профилей и групп, которые необходимо вернуть.
     * @return Ответ от VK API с данными записей со стены сообщества.
     */

    @GET("wall.get")
    suspend fun getWall(
        @Query("v") version: String,
        @Query("owner_id") ownerId: Int,
        @Query("access_token") accessToken: String,
        @Query("count") count: Int,
        @Query("offset") offset: Int,
        @Query("extended") extended: Int?,
        @Query("fields") fields: String?
    ): VkResponse<GroupWallResponse>


    @GET("wall.get")
    suspend fun getWallById(
        @Query("v") version: String,
        @Query("owner_id") groupId: Int,
        @Query("access_token") accessToken: String,
        @Query("offset") offset: Int,
        @Query("count") count: Int,
        @Query("extended") extended: Int?,
        @Query("fields") fields: String?
    ): VkResponse<GroupWallResponse>
}