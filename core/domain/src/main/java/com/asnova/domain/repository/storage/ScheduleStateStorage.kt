package com.asnova.domain.repository.storage

import com.asnova.model.LocalDate

interface ScheduleStateStorage {
    fun save(date: LocalDate)
    fun get() : LocalDate
}