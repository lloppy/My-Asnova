package com.asnova.model

// Паттерн Prototype
interface Prototype<T> {
    fun clone(): T
}