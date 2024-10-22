package com.asnova.domain.repository.firebase

import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite
import com.asnova.model.Resource
import com.asnova.model.ScheduleFirebase

interface ScheduleRepository {
    fun getPrivateSchedule(callback: (Resource<List<ScheduleAsnovaPrivate>>) -> Unit)
    fun getScheduleFromSite(callback: (Resource<List<ScheduleAsnovaSite>>) -> Unit)
    fun getAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit)
}