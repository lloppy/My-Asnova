package com.asnova.firebase.caldav

import com.asnova.domain.repository.firebase.CalendarService
import com.asnova.model.CalDavConfig
import com.asnova.model.ScheduleAsnovaPrivate
import java.time.LocalDate

class CalDavAdapter(private val calDavAdaptee: CalDavAdaptee) : CalendarService {
    override fun getPrivateSchedule(): List<ScheduleAsnovaPrivate> {
        val config = CalDavConfig(
            "https://calendar.mail.ru/principals/uc-ot.ru/mikhail/calendars/3bb28671-7672-4822-81d6-88806ee7b6cc/",
            "mikhail@uc-ot.ru",
            "cRcJLt8KqgR5QpLfQDiC"
        )
        return calDavAdaptee.getPrivateScheduleFromCalDav(config)
    }

    override fun getPrivateMapSchedule(): Map<LocalDate, List<ScheduleAsnovaPrivate>> {
        val config = CalDavConfig(
            "https://calendar.mail.ru/principals/uc-ot.ru/mikhail/calendars/3bb28671-7672-4822-81d6-88806ee7b6cc/",
            "mikhail@uc-ot.ru",
            "cRcJLt8KqgR5QpLfQDiC"
        )
        return calDavAdaptee.getPrivateScheduleMapFromCalDav(config)
    }
}
