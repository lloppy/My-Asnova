package com.asnova.model

data class AsnovaStudentsClass(
    val name: String = ""
) : Prototype<AsnovaStudentsClass> {
    override fun clone(): AsnovaStudentsClass {
        return this.copy()
    }
}