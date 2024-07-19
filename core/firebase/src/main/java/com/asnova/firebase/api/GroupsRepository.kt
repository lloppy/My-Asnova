package com.asnova.firebase.api

import com.asnova.model.WallItem

interface GroupsRepository {
    suspend fun getWallById(
        groupId: Int,
        offset: Int,
        count: Int,
        extended: Int?,
        fields: List<String>?
    ): List<WallItem>
}