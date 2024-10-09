package com.example.asnova.screen.chat.factory

// Паттерн Abstract factory

abstract class ChatFactory {
    abstract fun createChat(): Chat
}

class AdminChatFactory : ChatFactory() {
    override fun createChat(): Chat {
        return AdminChat()
    }
}

class GuestChatFactory : ChatFactory() {
    override fun createChat(): Chat {
        return GuestChat()
    }
}

class StudentChatFactory : ChatFactory() {
    override fun createChat(): Chat {
        return StudentChat()
    }
}