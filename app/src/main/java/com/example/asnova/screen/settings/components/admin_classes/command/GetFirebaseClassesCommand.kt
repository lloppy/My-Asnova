package com.example.asnova.screen.settings.components.admin_classes.command

class GetFirebaseClassesCommand : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onGetFirebaseClicked()
    }

    override fun undo() {
    }
}