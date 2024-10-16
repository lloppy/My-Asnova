package com.asnova.domain.repository.storage

import com.asnova.model.LocalDate

// Паттерн Bridge
interface ScheduleStateStorage : Storage<LocalDate>{
    override fun save(date: LocalDate)
    override fun get() : LocalDate
}