package com.example.asnova.screen.main.feed

import com.example.asnova.data.NewsItem

data class FeedState(
    val value: List<NewsItem> = emptyList(),
    val error: String = "",
    val loading: Boolean = false
)