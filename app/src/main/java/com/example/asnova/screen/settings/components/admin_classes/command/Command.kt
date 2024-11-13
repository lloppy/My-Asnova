package com.example.asnova.screen.settings.components.admin_classes.command

interface Command {
    fun execute(receiver: CommandReceiver)
    fun undo()
}