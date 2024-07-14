package com.example.asnova.screen.main.feed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asnova.screen.main.feed.api.GroupsInteractor
import com.example.asnova.screen.main.feed.api.SingleLiveEvent
import com.example.asnova.screen.main.feed.api.WallItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val groupsInteractor: GroupsInteractor
) : ViewModel() {
    private val _showMessage = SingleLiveEvent<String>()
    val showMessage: LiveData<String> = _showMessage

    private var wallId: Int = 162375388

    private val _state = mutableStateOf(FeedState())
    val state: State<FeedState> = _state

    private val _wallItems = MutableLiveData<List<WallItem>>()
    val wallItems: LiveData<List<WallItem>> get() = _wallItems

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _downloadMore = MutableLiveData(true)
    val downloadMore: LiveData<Boolean> get() = _downloadMore

    private val _showStartProgress = MutableLiveData(true)
    val showStartProgress: LiveData<Boolean> get() = _showStartProgress

    init {
        onUpdateWall()
        onDownloadMore()
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

    fun onDownloadMore(fromStart: Boolean = false) = viewModelScope.launch {
        try {
            _isLoading.value = true
            val list = wallItems.value?.toMutableList() ?: mutableListOf()
            if (list.isEmpty()) _showStartProgress.value = true
            val offset = if (fromStart) 0 else wallItems.value?.size ?: 0
            val loadedData = groupsInteractor.getGroupWall(wallId, offset)
            if (loadedData.isEmpty()) _downloadMore.value = false
            if (fromStart) _wallItems.value = loadedData.sortedByDescending { it.date }
            else _wallItems.value = list.apply { addAll(loadedData) }
                .distinctBy { it.id }.sortedByDescending { it.date }
        } catch (e: Exception) {
            _showMessage.postValue(e.message ?: "Unknown error occurred")
        } finally {
            _showStartProgress.value = false
            _isLoading.value = false
        }
    }


    fun pullToRefresh() {
        if (isLoading.value != true) {

            onUpdateWall()
            onDownloadMore(true)
        }
    }
}
