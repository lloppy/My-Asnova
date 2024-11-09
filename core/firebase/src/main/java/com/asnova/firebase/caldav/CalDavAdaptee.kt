package com.asnova.firebase.caldav

import com.asnova.model.CalDavConfig
import com.asnova.model.ScheduleAsnovaPrivate
import java.time.LocalDate

// Паттерн Adapter
// and this is the interface we want to use with the client
interface CalDavAdaptee {
    fun getPrivateScheduleFromCalDav(config: CalDavConfig): List<ScheduleAsnovaPrivate>
    fun getPrivateScheduleMapFromCalDav(config: CalDavConfig): Map<LocalDate, List<ScheduleAsnovaPrivate>>
}