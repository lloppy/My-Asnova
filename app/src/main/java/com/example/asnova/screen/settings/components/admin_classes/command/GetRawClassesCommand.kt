package com.example.asnova.screen.settings.components.admin_classes.command

import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

// Паттерн Command
class GetRawClassesCommand (
    private val callback: (Resource<List<AsnovaStudentsClass>>) -> Unit
) : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onGetRawClicked(callback)
    }

    override fun undo() {
    }
}