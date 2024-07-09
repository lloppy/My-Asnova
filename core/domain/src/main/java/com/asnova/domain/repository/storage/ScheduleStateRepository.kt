package com.asnova.domain.repository.storage

import com.asnova.model.LocalDate

interface ScheduleStateRepository {
    fun save(date: LocalDate)
    fun get() : LocalDate
}