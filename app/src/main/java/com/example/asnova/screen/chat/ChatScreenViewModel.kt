package com.example.asnova.screen.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.asnova.screen.chat.factory.Chat
import com.example.asnova.screen.chat.factory.ChatFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    chatFactory: ChatFactory
) : ViewModel() {
    private val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state

    // Паттерн Abstract factory
    // явно не указываем, для кого именно фабрику мы создаем
    private val chat: Chat = chatFactory.createChat()

    init {
        loadChannels()
    }

    private fun loadChannels() {
        val channels = chat.getChannels()
        _state.value.channels = channels
        loadMessages(channels.first().id)
    }

    private fun loadMessages(channelId: Long) {
        val messages = chat.getMessages(channelId)
        _state.value.messages = messages
    }

}