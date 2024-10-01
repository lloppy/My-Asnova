import android.util.Log
import com.asnova.model.Schedule
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.component.VEvent
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.StringReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/*
private val calDavClient = CalDavClient(
    "https://calendar.mail.ru/principals/vk.com/ankudinovazaecologiy/calendars/e44497c4-4978-4518-81de-0530cf40c794/",
    "ankudinovazaecologiy@vk.com",
    "FYnERU8DZC1zvTm12NV3"
)   */

class CalDavClient(
    private val baseUrl: String,
    private val username: String,
    private val password: String
) {
    private val client = OkHttpClient()

    private fun fetchCalendarData(): String? {
        // Паттерн Builder
        val request = Request.Builder()
            .url(baseUrl)
            .header("Authorization", Credentials.basic(username, password))
            .build()

        client.newCall(request).execute().use { response ->
            return if (!response.isSuccessful) null else response.body?.string()
        }
    }

    private fun parseCalendarData(data: String): Calendar {
        val stringReader = StringReader(data)

        // Паттерн Builder
        return CalendarBuilder().build(stringReader)
    }

    fun getScheduleList(): List<com.asnova.model.ScheduleAsnovaPrivate> {
        val calendarData = fetchCalendarData()

        val calendar = calendarData?.let { parseCalendarData(it) } ?: return emptyList()

        val events: List<VEvent> = calendar.components.filterIsInstance<VEvent>()
        return events.map { event ->
            Log.e(
                "calendar_info",
                "CREATED " + event.getProperty<Property>(Property.CREATED)?.value.toString()
            )
            Log.e(
                "calendar_info",
                "DTSTART " + event.getProperty<Property>(Property.DTSTART)?.value.toString()
            )
            Log.e(
                "calendar_info",
                "DTEND " + event.getProperty<Property>(Property.DTEND)?.value.toString()
            )

            Log.e("calendar_info", "\n")

            Log.e(
                "calendar_info",
                "parseDate CREATED " + parseDate(event.getProperty<Property>(Property.CREATED)?.value)
            )
            Log.e(
                "calendar_info",
                "parseDate DTSTART " + parseDate(event.getProperty<Property>(Property.DTSTART)?.value)
            )
            Log.e(
                "calendar_info",
                "parseDate DTEND " + parseDate(event.getProperty<Property>(Property.DTEND)?.value)
            )

            Log.e("calendar_info", "__________________")

            // Паттерн Static factory method
            Schedule.createPrivateSchedule(
                summary = event.getProperty<Property>(Property.SUMMARY)?.value,
                created = parseDate(event.getProperty<Property>(Property.CREATED)?.value),
                start = parseDate(event.getProperty<Property>(Property.DTSTART)?.value),
                end = parseDate(event.getProperty<Property>(Property.DTEND)?.value),
                uid = event.getProperty<Property>(Property.UID)?.value.toString()
            )
        }
    }

    private fun parseDate(dateStr: String?): LocalDateTime {
        val createdFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
        val fallbackDate = LocalDateTime.of(2004, 4, 17, 0, 0)

        if (dateStr.isNullOrEmpty()) return fallbackDate

        return try {
            when {
                dateStr.endsWith("Z") -> LocalDateTime.parse(dateStr, createdFormatter)
                    .atOffset(ZoneOffset.UTC).toLocalDateTime()

                else -> {
                    val datePart = dateStr.substring(0, 8)
                    val timePart = dateStr.substring(9)
                    val date = LocalDate.parse(datePart, DateTimeFormatter.BASIC_ISO_DATE)
                    val time = LocalTime.parse(timePart, DateTimeFormatter.ofPattern("HHmmss"))
                    LocalDateTime.of(date, time)
                }
            }
        } catch (e: Exception) {
            fallbackDate
        }
    }
}
