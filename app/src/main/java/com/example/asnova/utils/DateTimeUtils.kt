package com.example.asnova.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

fun formatTime(date: Date): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}

fun formatRelativeDate(date: Date): String {
    val now = LocalDate.now()
    val givenDate = Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()

    return when (val daysBetween = ChronoUnit.DAYS.between(givenDate, now).toInt()) {
        0 -> "Сегодня"
        1 -> "Вчера"
        2 -> "Позавчера"
        3, 4 -> "$daysBetween дня назад"
        else -> "$daysBetween дней назад"
    }
}

fun getDayOfWeekInRussian(date: LocalDate): String {
    return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")).lowercase()
}