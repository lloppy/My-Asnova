package com.example.asnova.screen.settings.components.admin_classes.command


class GetRawClassesCommand : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onGetRawClicked()
    }

    override fun undo() {
    }
}