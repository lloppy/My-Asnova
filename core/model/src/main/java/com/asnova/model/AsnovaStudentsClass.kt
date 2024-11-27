package com.asnova.model

// Паттерн Memento
data class AsnovaStudentsClass(
    val name: String = ""
) : Prototype<AsnovaStudentsClass> {
    override fun clone(): AsnovaStudentsClass {
        return this.copy()
    }

    fun createMemento(): AsnovaStudentsClassMemento {
        return AsnovaStudentsClassMemento(this.name)
    }

    fun restoreMemento(memento: AsnovaStudentsClassMemento): AsnovaStudentsClass {
        return this.copy(name = memento.name)
    }
}