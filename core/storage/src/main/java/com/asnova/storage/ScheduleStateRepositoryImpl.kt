package com.asnova.storage

import com.asnova.model.LocalDate
import com.asnova.domain.repository.storage.ScheduleStateRepository
import com.asnova.domain.repository.storage.ScheduleStateStorage

class ScheduleStateRepositoryImpl(
    private val scheduleStateStorage: ScheduleStateStorage
) : ScheduleStateRepository {
    override fun save(date: LocalDate)
    {
        scheduleStateStorage.save(date)
    }
    override fun get() : LocalDate
    {
        return scheduleStateStorage.get()
    }
}