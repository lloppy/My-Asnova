package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.Resource
import com.asnova.model.ScheduleFirebase

class AddNewLessonUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(lesson: ScheduleFirebase, callback: (Resource<Boolean>) -> Unit) {
        return scheduleRepository.addNewLesson(lesson, callback)
    }
}