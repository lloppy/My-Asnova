package com.example.asnova.screen.settings.components.admin_classes.command

class DeleteClassesFromFirebaseCommand: Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onDeleteAllClicked()
    }

    override fun undo() {
    }
}