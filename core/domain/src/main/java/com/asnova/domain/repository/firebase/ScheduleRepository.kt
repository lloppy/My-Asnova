package com.asnova.domain.repository.firebase

import com.asnova.model.Resource
import com.asnova.model.Schedule

interface ScheduleRepository {
    fun addNewLesson(schedule: Schedule, callback: (Resource<Boolean>) -> Unit)
    fun getAllSchedule(callback: (Resource<List<Schedule>>) -> Unit)
}