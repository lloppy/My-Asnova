package com.example.asnova.screen.settings.components.admin_classes.command

// Паттерн Command
class DeleteClassesFromFirebaseCommand(
    private val callback: (Boolean) -> Unit
): Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onDeleteAllClicked(callback)
    }

    override fun undo() {
    }
}