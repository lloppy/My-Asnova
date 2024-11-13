package com.example.asnova.screen.settings.components.admin_classes.command

class PushClassesToFirebaseCommand : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onSaveClicked()
    }

    override fun undo() {
    }
}