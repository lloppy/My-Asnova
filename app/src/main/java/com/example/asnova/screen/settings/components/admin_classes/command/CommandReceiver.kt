package com.example.asnova.screen.settings.components.admin_classes.command

import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

// Паттерн Command
interface CommandReceiver {
    fun onDeleteAllClicked(callback: (Boolean) -> Unit)

    fun onGetRawClicked(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit)
    fun onGetFirebaseClicked(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit)

    fun onSaveClicked(asnovaClasses: List<AsnovaStudentsClass>?, onSuccess: () -> Unit)
    fun onDeleteClicked()

    fun processCommand(command: Command) {
        command.execute(this)
    }
}