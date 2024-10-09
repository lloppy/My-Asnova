package com.example.asnova.screen.chat

import com.asnova.model.User


data class ChatState(
    var channels: List<Channel> = emptyList(),
    var messages: List<Message> = emptyList()
)


data class Message(val id: Long, val text: String)
data class Channel(val id: Long, val name: String, val users: List<User>)

