package com.asnova.firebase.caldav

import com.asnova.domain.repository.firebase.CalendarService
import com.asnova.model.CalDavConfig
import com.asnova.model.ScheduleAsnovaPrivate

// Паттерн Adapter
// the Adapter implementing Target interface and taking the Adaptee in the constructor
class CalDavAdapter(private val calDavAdaptee: CalDavAdaptee) : CalendarService {
    override fun getPrivateSchedule(): List<ScheduleAsnovaPrivate> {
        val config = CalDavConfig(
            "https://calendar.mail.ru/principals/uc-ot.ru/mikhail/calendars/3bb28671-7672-4822-81d6-88806ee7b6cc/",
            "mikhail@uc-ot.ru",
            "cRcJLt8KqgR5QpLfQDiC"
        )

        // calling method from adapted interface
        return calDavAdaptee.getPrivateScheduleFromCalDav(config)
    }
}
