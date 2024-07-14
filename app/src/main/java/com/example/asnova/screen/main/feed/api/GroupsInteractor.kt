package com.example.asnova.screen.main.feed.api

import javax.inject.Inject

class GroupsInteractor @Inject constructor(
    private val groupsRepository: GroupsRepository
) {
    companion object {
        private const val EXTENDED = 1
        const val LIKE_TYPE_POST = "post"
        const val FROM_GROUP = 1
        const val MAX_PICTURES_PER_POST = 10
        const val VK_URL = "https://vk.com"
    }
    suspend fun getGroupWall(id: Int, offset: Int): List<WallItem> {
        val count = 10
        val fields = listOf("photo_50", "name")
        val extended = 1
        return groupsRepository.getWallById(id, offset, count, extended, fields)
            .map { it.copy(text = parseText(it.text)) }
    }
    private fun parseText(text: String): String {
        val pattern = "\\[(.*?)\\|(.*?)\\]".toRegex()
        return pattern.replace(text, "[$VK_URL/$1|$2]")
    }
}