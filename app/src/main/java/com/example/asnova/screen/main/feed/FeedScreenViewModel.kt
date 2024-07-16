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
import com.example.asnova.screen.main.schedule.AsnovaScheduleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val groupsInteractor: GroupsInteractor
) : ViewModel() {
    private val _state = mutableStateOf(FeedState())
    val state: State<FeedState> = _state

    private val _wallItems = MutableLiveData<List<WallItem>>()
    val wallItems: LiveData<List<WallItem>> get() = _wallItems

    private val _ohranaWallItems = MutableLiveData<List<WallItem>>()
    val ohranaWallItems: LiveData<List<WallItem>> get() = _ohranaWallItems

    private val _downloadMore = MutableLiveData(true)
    val downloadMore: LiveData<Boolean> get() = _downloadMore

    private val _showStartProgress = MutableLiveData(true)
    val showStartProgress: LiveData<Boolean> get() = _showStartProgress

    private val _showMessage = SingleLiveEvent<String>()
    val showMessage: LiveData<String> = _showMessage

    init {
        onUpdateWall(wallId = 162375388)
        onUpdateWall(wallId = 80108699)
    }

    private fun onUpdateWall(wallId: Int) = viewModelScope.launch {
        try {
            val list = if (wallId == 162375388) {
                _wallItems.value?.toMutableList() ?: mutableListOf()
            } else {
                _ohranaWallItems.value?.toMutableList() ?: mutableListOf()
            }
            val loadedData = groupsInteractor.getGroupWall(wallId, 0)

            when (wallId) {
                162375388 -> {
                    if (list.isEmpty()) {
                        _wallItems.value = loadedData.sortedByDescending { it.date }
                    } else {
                        _wallItems.value = list.apply { addAll(loadedData) }
                            .distinctBy { it.id }.sortedByDescending { it.date }
                    }
                }

                80108699 -> {
                    if (list.isEmpty()) {
                        _ohranaWallItems.value = loadedData.sortedByDescending { it.date }
                    } else {
                        _ohranaWallItems.value = list.apply { addAll(loadedData) }
                            .distinctBy { it.id }.sortedByDescending { it.date }
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("vk_info", "Error fetching wall data: ${e.message}", e)
            _showMessage.postValue(e.message ?: "Unknown error occurred")
        }
    }

    fun onDownloadMore(fromStart: Boolean = false, wallId: Int) = viewModelScope.launch {
        try {
            _state.value = FeedState(loading = true)

            val list = if (wallId == 162375388) {
                _wallItems.value?.toMutableList() ?: mutableListOf()
            } else if (wallId == 80108699) {
                _ohranaWallItems.value?.toMutableList() ?: mutableListOf()
            } else {
                mutableListOf()
            }

            if (list.isEmpty()) _showStartProgress.value = true
            val offset = if (fromStart) 0 else list.size
            val loadedData = groupsInteractor.getGroupWall(wallId, offset)
            if (loadedData.isEmpty()) _downloadMore.value = false

            when (wallId) {
                162375388 -> {
                    if (fromStart) _wallItems.value = loadedData.sortedByDescending { it.date }
                    else _wallItems.value = list.apply { addAll(loadedData) }.distinctBy { it.id }
                        .sortedByDescending { it.date }
                }

                80108699 -> {
                    if (fromStart) _ohranaWallItems.value =
                        loadedData.sortedByDescending { it.date }
                    else _ohranaWallItems.value =
                        list.apply { addAll(loadedData) }.distinctBy { it.id }
                            .sortedByDescending { it.date }
                }
            }
        } catch (e: Exception) {
            Log.e("vk_info", "Error fetching wall data: ${e.message}", e)
            _showMessage.postValue(e.message ?: "Unknown error occurred")
            _state.value = FeedState(error = e.message ?: "An unexpected error occurred")

        } finally {
            _state.value = FeedState(loading = false)
            _showStartProgress.value = false
        }
    }

    fun pullToRefresh() = viewModelScope.launch {
        try {
            onUpdateWall(162375388)
            onUpdateWall(80108699)
        } catch (_: Exception) {
        }
    }
}
