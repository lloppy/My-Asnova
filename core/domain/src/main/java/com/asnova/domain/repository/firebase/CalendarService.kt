package com.asnova.domain.repository.firebase

import com.asnova.model.ScheduleAsnovaPrivate

// Паттерн Adapter
// the client will accept only this interface
// по гайду https://swiderski.tech/kotlin-adapter-pattern/
interface CalendarService {
    fun getPrivateSchedule(): List<ScheduleAsnovaPrivate>
}