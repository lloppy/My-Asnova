package com.asnova.domain.repository.storage

interface IsAuthedUserStorage {
    fun save(status: String)
    fun get() : String
}