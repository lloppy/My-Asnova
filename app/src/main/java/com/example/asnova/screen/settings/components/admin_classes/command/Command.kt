package com.example.asnova.screen.settings.components.admin_classes.command

// Паттерн Command
interface Command {
    fun execute(receiver: CommandReceiver)
    fun undo()
}