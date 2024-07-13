package com.example.asnova.screen.main.feed.api

interface GroupsRepository {
    suspend fun getWallById(
        groupId: Int,
        offset: Int,
        count: Int,
        extended: Int?,
        fields: List<String>?
    ): List<WallItem>
}