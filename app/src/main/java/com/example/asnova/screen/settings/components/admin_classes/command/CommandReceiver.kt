package com.example.asnova.screen.settings.components.admin_classes.command

interface CommandReceiver {
    fun onAddClicked()

    fun onDeleteClicked()
    fun onDeleteAllClicked()

    fun onGetRawClicked()
    fun onGetFirebaseClicked()

    fun onSaveClicked()
    fun processCommand(command: Command) {
        command.execute(this)
    }
}