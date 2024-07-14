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

    private val _details = MutableLiveData<GroupDetail>()
    val details: LiveData<GroupDetail> get() = _details

    private val _wallItems = MutableLiveData<List<WallItem>>()
    val wallItems: LiveData<List<WallItem>> get() = _wallItems

    init {
        onUpdateWall()
        getGroupById()
    }

    private fun getGroupById() {
        viewModelScope.launch {
            try {
                _details.value = groupsInteractor.getGroupById(wallId)
            } catch (ex: Exception) {
                showMessage(ex.message ?: "")
            }
        }
    }

    private fun onUpdateWall() = viewModelScope.launch {
        try {
            val list = wallItems.value?.toMutableList() ?: mutableListOf()
            val loadedData = groupsInteractor.getGroupWall(wallId, 0)
            Log.d("vk_info", "Loaded ${loadedData.size} items: $loadedData")
            if (list.isEmpty()) {
                _wallItems.value = loadedData.sortedByDescending { it.date }
            } else {
                _wallItems.value = list.apply { addAll(loadedData) }
                    .distinctBy { it.id }.sortedByDescending { it.date }
            }
        } catch (e: Exception) {
            Log.e("vk_info", "Error fetching wall data: ${e.message}", e)
            _showMessage.postValue(e.message ?: "Unknown error occurred")
        }
    }


    fun pullToRefresh() {
        onUpdateWall()
    }
}
