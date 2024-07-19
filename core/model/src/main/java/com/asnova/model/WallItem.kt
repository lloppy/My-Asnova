package com.asnova.model

import java.util.Date

data class WallItem(
    val id: Int,
    val text: String,
    val title: String,
    val withoutTitle: String,
    val date: Date,
    var userLikes: Int,
    var likesCount: Int,
    val images: List<WallImageItem>,
    val hashtags: List<String> = emptyList(),
    val postUrl: String
)

data class WallImageItem(
    val id: Int,
    val height: Int,
    val width: Int,
    val url: String,
)
