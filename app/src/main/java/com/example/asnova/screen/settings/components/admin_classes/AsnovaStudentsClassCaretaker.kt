package com.example.asnova.screen.settings.components.admin_classes

import android.util.Log
import com.asnova.model.AsnovaStudentsClassMemento

// Паттерн Memento
class AsnovaStudentsClassCaretaker {
    private val mementos = mutableListOf<AsnovaStudentsClassMemento>()

    fun saveMemento(memento: AsnovaStudentsClassMemento) {
        mementos.add(memento)
        Log.i(MEMENTO_TAG, "saveMemento: ${memento.name}")
        Log.d(MEMENTO_TAG, "saveMemento - total size: ${mementos.size}")
    }

    fun restoreMemento(): AsnovaStudentsClassMemento? {
        return if (mementos.isNotEmpty()) {
            Log.i(MEMENTO_TAG, "restoreMemento: remove ${mementos[mementos.size - 1]}")
            Log.d(MEMENTO_TAG, "restoreMemento - total size: ${mementos.size}")
            mementos.removeAt(mementos.size - 1)
        } else {
            Log.e(MEMENTO_TAG, "restoreMemento: remove empty")
            null
        }
    }

    companion object {
        const val MEMENTO_TAG = "MementoPatternTag"
    }
}