package com.example.asnova.screen.main.feed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asnova.data.NewsItem
import com.example.asnova.screen.main.feed.api.GroupsInteractor
import com.example.asnova.screen.main.feed.api.SingleLiveEvent
import com.example.asnova.screen.main.feed.api.WallItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val groupsInteractor: GroupsInteractor
): ViewModel() {
    private val _showMessage = SingleLiveEvent<String>()
    val showMessage: LiveData<String> = _showMessage

    private var wallId: Int = 221091451

    private val _state = mutableStateOf(FeedState())
    val state: State<FeedState> = _state

    private val _wallItems = MutableLiveData<List<WallItem>>()
    val wallItems: LiveData<List<WallItem>> get() = _wallItems

    init {
        onUpdateWall()
        fetchTelegramMessages()
    }


    fun onUpdateWall() = viewModelScope.launch {
        try {
            val list = wallItems.value?.toMutableList() ?: mutableListOf()
            val loadedData = groupsInteractor.getGroupWall(wallId, 0)
            if (list.isEmpty()) _wallItems.value = loadedData.sortedByDescending { it.date }
            else _wallItems.value = list.apply { addAll(loadedData) }
                .distinctBy { it.id }.sortedByDescending { it.date }
            Log.e("vk_info", "news is ${wallItems.value?.firstOrNull()?.text.toString()}")
        } catch (e: Exception) {
            //showMessage(e.message ?:"")
        }
    }

    private fun fetchTelegramMessages() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = "")
            try {
                val messages = withContext(Dispatchers.IO) {
                    getTelegramMessages()
                }
                _state.value = _state.value.copy(
                    loading = false,
                    value = messages.mapIndexed { index, message ->
                        NewsItem(
                            id = index.toString(),
                            image = "https://asnova.pro/thumb/2/5DD6qxOMlnoQps2Rxbj3wA/400r/d/l.jpg",
                            title = getHeadline(message),
                            content = message
                        )
                    })
            } catch (e: Exception) {
                _state.value =
                    _state.value.copy(loading = false, error = "Error fetching news: ${e.message}")
                Log.e("FeedScreenViewModel", "Error fetching news", e)
            }
        }
    }

    private fun getTelegramMessages(): List<String> {
        val url = "https://t.me/s/asnova_pro"
        val document = Jsoup.connect(url).get()
        val messagesElements = document.select(".tgme_widget_message_text")
        val uniqueMessages = mutableSetOf<String>()
        messagesElements.forEach { element ->
            val messageText = element.text()
            val headline = getHeadline(messageText)
            val fullMessage = "$headline\n$messageText"
            uniqueMessages.add(fullMessage)
        }
        return uniqueMessages.toList()
    }

    private fun getHeadline(messageText: String): String {
        val regex = Regex(".*?[.!?\\s](?=\\p{Punct}|\\p{So}|\\p{Sc}|\\s|\\z)")
        val match = regex.find(messageText)
        return match?.value ?: messageText
    }

    fun pullToRefresh() {
        fetchTelegramMessages()
    }
}
