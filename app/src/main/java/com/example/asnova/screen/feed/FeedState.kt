package com.example.asnova.screen.feed

import com.asnova.model.WallItem

data class FeedState(
    val news: List<WallItem> = emptyList(),
    val error: String = "",
    val loading: Boolean = false
)