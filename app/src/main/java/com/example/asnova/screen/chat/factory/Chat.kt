package com.example.asnova.screen.chat.factory

import com.asnova.model.Role
import com.asnova.model.User
import com.example.asnova.screen.chat.Channel
import com.example.asnova.screen.chat.Message

// Паттерн Abstract factory

interface Chat {
    fun getChannels(): List<Channel>
    fun getMessages(channelId: Long): List<Message>
}

class AdminChat : Chat {
    override fun getChannels(): List<Channel> {
        return listOf(
            Channel(
                1, "Admin Channel", users = listOf(
                    User(
                        userUid = "1",
                        username = "student_name",
                        email = "student_user1@email.com",
                        role = Role.STUDENT
                    ),
                    User(
                        userUid = "1",
                        username = "worker_name",
                        email = "worker_user1@email.com",
                        role = Role.STUDENT
                    )
                )
            )
        )
    }

    override fun getMessages(channelId: Long): List<Message> {
        return listOf(Message(1, "Welcome to the admin channel!"))
    }
}

class GuestChat : Chat {
    override fun getChannels(): List<Channel> {
        return listOf(
            Channel(
                2, "Guest Channel", listOf(
                    User(
                        userUid = "1",
                        username = "admin",
                        email = "admin_official@email.com",
                        role = Role.ADMIN
                    )
                )
            )
        )
    }


    override fun getMessages(channelId: Long): List<Message> {
        return listOf(Message(2, "Hello guest!"))
    }
}

class StudentChat : Chat {
    override fun getChannels(): List<Channel> {
        return listOf(
            Channel(
                3, "Student Channel", listOf(
                    User(
                        userUid = "1",
                        username = "admin",
                        email = "admin_official@email.com",
                        role = Role.ADMIN
                    )
                )
            )
        )
    }

    override fun getMessages(channelId: Long): List<Message> {
        return listOf(Message(3, "Welcome to the student channel!"))
    }
}