package com.example.asnova.screen.main.feed.api

data class WallItem(
    val id: Int,
    val text: String,
    val title: String,
    val withoutTitle: String,
    val posterName: String,
    val posterThumbnail: String,
    val date: Int,
    var userLikes: Int,
    var likesCount: Int,
    val images: List<WallImageItem>,
    val hashtags: List<String> = emptyList()
)

data class WallImageItem(
    val id: Int,
    val height: Int,
    val width: Int,
    val url: String,
)