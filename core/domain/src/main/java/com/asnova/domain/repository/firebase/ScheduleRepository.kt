package com.asnova.domain.repository.firebase

import com.asnova.model.AsnovaSchedule
import com.asnova.model.AsnovaSiteSchedule
import com.asnova.model.Resource
import com.asnova.model.Schedule

interface ScheduleRepository {
    fun addNewLesson(schedule: Schedule, callback: (Resource<Boolean>) -> Unit)
    fun getAllScheduleFromFirebase(callback: (Resource<List<Schedule>>) -> Unit)
    fun getScheduleFromCalDav(callback: (Resource<List<AsnovaSchedule>>) -> Unit)
    fun getAllSchedule(callback: (Resource<List<Schedule>>) -> Unit)
    fun getScheduleFromSite(callback: (Resource<List<AsnovaSiteSchedule>>) -> Unit)
}