package com.asnova.domain.repository.firebase

import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

interface AsnovaClassRepository {
    fun getAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit)
    fun createAsnovaClass(newClass: AsnovaStudentsClass, callback: (Resource<AsnovaStudentsClass>) -> Unit)
    fun updateAsnovaClass(updatedClass: AsnovaStudentsClass, callback: (Resource<AsnovaStudentsClass>) -> Unit)
    fun deleteAsnovaClass(classId: Int, callback: (Resource<Unit>) -> Unit)

    fun duplicateAsnovaClass(updatedClass: AsnovaStudentsClass)
}