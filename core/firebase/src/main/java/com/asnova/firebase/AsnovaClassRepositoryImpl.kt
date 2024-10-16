package com.asnova.firebase

import com.asnova.domain.repository.firebase.AsnovaClassRepository
import com.asnova.domain.usecase.CreateAsnovaClassUseCase
import com.asnova.domain.usecase.DeleteAsnovaClassUseCase
import com.asnova.domain.usecase.GetAsnovaClassesUseCase
import com.asnova.domain.usecase.UpdateAsnovaClassUseCase
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

class AsnovaClassRepositoryImpl(
    private val getAsnovaClassesUseCase: GetAsnovaClassesUseCase,
    private val createAsnovaClassUseCase: CreateAsnovaClassUseCase,
    private val updateAsnovaClassUseCase: UpdateAsnovaClassUseCase,
    private val deleteAsnovaClassUseCase: DeleteAsnovaClassUseCase,
) : AsnovaClassRepository {

    override fun getAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        getAsnovaClassesUseCase.invoke { result ->
            callback(result)
        }
    }

    override fun duplicateAsnovaClass(updatedClass: AsnovaStudentsClass) {
        throw NotImplementedError("This method is no longer needed in the repository.")
    }

    override fun createAsnovaClass(newClass: AsnovaStudentsClass, callback: (Resource<AsnovaStudentsClass>) -> Unit) {
        createAsnovaClassUseCase.invoke(newClass) { result ->
            callback(result)
        }
    }

    override fun updateAsnovaClass(updatedClass: AsnovaStudentsClass, callback: (Resource<AsnovaStudentsClass>) -> Unit) {
        updateAsnovaClassUseCase.invoke(updatedClass) { result ->
            callback(result)
        }
    }

    override fun deleteAsnovaClass(classId: Int, callback: (Resource<Unit>) -> Unit) {
        deleteAsnovaClassUseCase.invoke(classId) { result ->
            callback(result)
        }
    }
}