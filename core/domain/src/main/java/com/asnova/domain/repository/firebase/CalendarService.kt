package com.asnova.domain.repository.firebase

import com.asnova.model.ScheduleAsnovaPrivate
import java.time.LocalDate

// Паттерн Adapter
// the client will accept only this interface
// по гайду https://swiderski.tech/kotlin-adapter-pattern/
interface CalendarService {
    fun getPrivateSchedule(): List<ScheduleAsnovaPrivate>
    fun getPrivateMapSchedule(): Map<LocalDate, List<ScheduleAsnovaPrivate>>
}