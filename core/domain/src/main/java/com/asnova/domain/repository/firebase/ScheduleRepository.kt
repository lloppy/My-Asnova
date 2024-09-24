package com.asnova.domain.repository.firebase

import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite
import com.asnova.model.Resource
import com.asnova.model.Schedule

interface ScheduleRepository {
    fun addNewLesson(schedule: Schedule, callback: (Resource<Boolean>) -> Unit)
    fun getAllScheduleFromFirebase(callback: (Resource<List<Schedule>>) -> Unit)
    fun getScheduleFromCalDav(callback: (Resource<List<ScheduleAsnovaPrivate>>) -> Unit)
    fun getAllSchedule(callback: (Resource<List<Schedule>>) -> Unit)
    fun getScheduleFromSite(callback: (Resource<List<ScheduleAsnovaSite>>) -> Unit)
}