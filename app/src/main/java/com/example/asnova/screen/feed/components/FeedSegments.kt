package com.example.asnova.screen.feed.components

// Паттерн Singleton
object Segments {
    const val MY_GROUP = "Моя группа"
    const val WORK_PROFESSIONS = "Рабочие профессии"
    const val SAFETY = "Охрана труда"

    val all = listOf(MY_GROUP, WORK_PROFESSIONS, SAFETY)
    val forVisitor = listOf(WORK_PROFESSIONS, SAFETY)
}