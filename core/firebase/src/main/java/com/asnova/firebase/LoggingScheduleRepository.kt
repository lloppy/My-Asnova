package com.asnova.firebase

import android.util.Log
import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite

// Паттерн Proxy
class LoggingScheduleRepository(private val repository: ScheduleRepository) : ScheduleRepository {

    private val tag = "LoggingScheduleRepository"

    private fun <T> logResourceResult(methodName: String, resource: Resource<T>) {
        when (resource) {
            is Resource.Loading -> Log.d(tag, "$methodName called - Loading")
            is Resource.Success -> Log.d(tag, "$methodName result: ${resource.data}, message: ${resource.message}")
            is Resource.Error -> Log.e(tag, "$methodName error: ${resource.message}")
        }
    }

    override fun clearAsnovaClasses(callback: (Boolean) -> Unit) {
        Log.d(tag, "clearAsnovaClasses called")
        repository.clearAsnovaClasses { result ->
            Log.d(tag, "clearAsnovaClasses result: $result")
            callback(result)
        }
    }

    override fun getPrivateSchedule(callback: (Resource<List<ScheduleAsnovaPrivate>>) -> Unit) {
        Log.d(tag, "getPrivateSchedule called")
        callback(Resource.Loading())
        repository.getPrivateSchedule { resource ->
            logResourceResult("getPrivateSchedule", resource)
            callback(resource)
        }
    }

    override fun getScheduleFromSite(callback: (Resource<List<ScheduleAsnovaSite>>) -> Unit) {
        Log.d(tag, "getScheduleFromSite called")
        callback(Resource.Loading())
        repository.getScheduleFromSite { resource ->
            logResourceResult("getScheduleFromSite", resource)
            callback(resource)
        }
    }

    override fun getRawAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        Log.d(tag, "getRawAsnovaClasses called")
        repository.getRawAsnovaClasses { resource ->
            logResourceResult("getRawAsnovaClasses", resource)
            callback(resource)
        }
    }

    override fun getAsnovaClassesFromFirebase(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        Log.d(tag, "getAsnovaClassesFromFirebase called")
        callback(Resource.Loading())
        repository.getAsnovaClassesFromFirebase { resource ->
            logResourceResult("getAsnovaClassesFromFirebase", resource)
            callback(resource)
        }
    }

    override fun pushAsnovaClasses(asnovaClasses: List<AsnovaStudentsClass>, callback: (Resource<Boolean>) -> Unit) {
        Log.d(tag, "pushAsnovaClasses called with ${asnovaClasses.size} classes")
        callback(Resource.Loading())
        repository.pushAsnovaClasses(asnovaClasses) { resource ->
            logResourceResult("pushAsnovaClasses", resource)
            callback(resource)
        }
    }
}