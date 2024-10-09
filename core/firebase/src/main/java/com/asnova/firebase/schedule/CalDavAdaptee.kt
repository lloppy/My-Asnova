package com.asnova.firebase.schedule

import com.asnova.model.CalDavConfig
import com.asnova.model.ScheduleAsnovaPrivate

// Паттерн Adapter
// and this is the interface we want to use with the client
interface CalDavAdaptee {
    fun getPrivateScheduleFromCalDav(config: CalDavConfig): List<ScheduleAsnovaPrivate>
}