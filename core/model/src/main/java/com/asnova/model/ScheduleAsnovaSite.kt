package com.asnova.model

data class ScheduleAsnovaSite(
    val dateRange: String,
    val year: Int,
    val timeRange: String,
    val description: String,
    val imageUrl: String,
    val newsLink: String
)