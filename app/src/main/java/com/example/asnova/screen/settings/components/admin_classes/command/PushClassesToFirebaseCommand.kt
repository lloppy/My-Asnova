package com.example.asnova.screen.settings.components.admin_classes.command

import com.asnova.model.AsnovaStudentsClass

// Паттерн Command
class PushClassesToFirebaseCommand(
    private val asnovaClasses: List<AsnovaStudentsClass>?,
    private val onSuccess: () -> Unit
) : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onSaveClicked(asnovaClasses, onSuccess)
    }

    override fun undo() {
    }
}