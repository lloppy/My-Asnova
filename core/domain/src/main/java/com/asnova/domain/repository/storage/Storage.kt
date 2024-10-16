package com.asnova.domain.repository.storage

// Паттерн Bridge
interface Storage<T> {
    fun save(data: T)
    fun get(): T
}