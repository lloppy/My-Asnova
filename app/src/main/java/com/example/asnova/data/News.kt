package com.example.asnova.data

data class NewsItem(
    val image: String = "",
    val title: String = "",
    val published: Long = 0,
    val content: String = "",
    val authors: List<String> = emptyList(),
    val gallery: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val id: String = "",
)
