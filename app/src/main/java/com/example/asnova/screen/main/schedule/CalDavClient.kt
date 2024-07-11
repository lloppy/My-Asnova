package com.example.asnova.screen.main.schedule

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Credentials
import java.io.StringReader

class CalDavClient(private val baseUrl: String, private val username: String, private val password: String) {

    private val client = OkHttpClient()

    fun fetchCalendarData(): String? {
        val request = Request.Builder()
            .url(baseUrl)
            .header("Authorization", Credentials.basic(username, password))
            .build()

        client.newCall(request).execute().use { response ->
            return if (!response.isSuccessful) null else response.body?.string()
        }
    }

    fun parseCalendarData(data: String): Calendar {
        val stringReader = StringReader(data)
        val calendarBuilder = CalendarBuilder()
        return calendarBuilder.build(stringReader)
    }
}
