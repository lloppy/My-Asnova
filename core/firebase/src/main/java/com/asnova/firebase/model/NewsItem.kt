package com.asnova.firebase.model

import com.google.firebase.Timestamp

internal data class NewsItem(
    val image: String = "",
    val title: String = "",
    val published: Timestamp = Timestamp.now(),
    val content: String = "",
    val authors: List<String> = emptyList(),
    val gallery: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val id: String = "",
)
