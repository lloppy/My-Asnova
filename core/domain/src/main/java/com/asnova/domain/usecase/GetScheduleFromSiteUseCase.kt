package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.ScheduleAsnovaSite
import com.asnova.model.Resource

class GetScheduleFromSiteUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(callback: (Resource<List<ScheduleAsnovaSite>>) -> Unit) {
        return scheduleRepository.getScheduleFromSite(callback)
    }
}