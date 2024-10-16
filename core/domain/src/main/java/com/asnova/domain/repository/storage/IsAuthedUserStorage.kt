package com.asnova.domain.repository.storage

// Паттерн Bridge
interface IsAuthedUserStorage : Storage<String> {
    override fun save(status: String)
    override fun get() : String
}