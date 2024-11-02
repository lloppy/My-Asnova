package com.asnova.domain.repository.firebase

import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite

interface ScheduleRepository {
    fun getPrivateSchedule(callback: (Resource<List<ScheduleAsnovaPrivate>>) -> Unit)
    fun getScheduleFromSite(callback: (Resource<List<ScheduleAsnovaSite>>) -> Unit)

    fun getRawAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit)
    fun getAsnovaClassesFromFirebase(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit)

    fun clearAsnovaClasses(callback: (Boolean) -> Unit)

    fun pushAsnovaClasses(
        asnovaClasses: List<AsnovaStudentsClass>,
        callback: (Resource<Boolean>) -> Unit
    )
}