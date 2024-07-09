package com.asnova.firebase.model

import com.asnova.model.ScheduleTask
import com.google.firebase.Timestamp

data class Schedule(
    val id: String = "",
    val date: Timestamp = Timestamp.now(),
    val start: Timestamp = Timestamp.now(),
    val end: Timestamp = Timestamp.now(),
    val lesson: String = "",
    val status: Int = 0,
    val classRoom: String = "",
    val teacher: String = "",
    val grade: Int = 1,
    val task: ScheduleTask = ScheduleTask(),
    val homeWork: List<String> = emptyList()
)
