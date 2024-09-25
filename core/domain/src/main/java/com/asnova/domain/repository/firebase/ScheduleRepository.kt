package com.asnova.domain.repository.firebase

import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite
import com.asnova.model.Resource
import com.asnova.model.ScheduleFirebase

interface ScheduleRepository {
    fun addNewLesson(scheduleFirebase: ScheduleFirebase, callback: (Resource<Boolean>) -> Unit)
    fun getAllScheduleFromFirebase(callback: (Resource<List<ScheduleFirebase>>) -> Unit)
    fun getScheduleFromCalDav(callback: (Resource<List<ScheduleAsnovaPrivate>>) -> Unit)
    fun getAllSchedule(callback: (Resource<List<ScheduleFirebase>>) -> Unit)
    fun getScheduleFromSite(callback: (Resource<List<ScheduleAsnovaSite>>) -> Unit)
}