package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.AsnovaSiteSchedule
import com.asnova.model.Resource

class GetScheduleFromSiteUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(callback: (Resource<List<AsnovaSiteSchedule>>) -> Unit) {
        return scheduleRepository.getScheduleFromSite(callback)
    }
}