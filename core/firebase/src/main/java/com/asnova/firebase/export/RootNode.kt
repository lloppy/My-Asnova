package com.asnova.firebase.export

// Паттерн Composite
class RootNode : DatabaseNode {
    private val children = mutableListOf<DatabaseNode>()

    override fun getData(): List<Any> {
        return children.map { it.getData() }
    }

    fun addChild(child: DatabaseNode) {
        children.add(child)
    }
}