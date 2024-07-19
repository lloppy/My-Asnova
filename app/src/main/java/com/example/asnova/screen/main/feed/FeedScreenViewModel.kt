package com.example.asnova.screen.main.feed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asnova.domain.usecase.GetAsnovaNewsUseCase
import com.asnova.domain.usecase.GetSafetyNewsUseCase
import com.asnova.domain.usecase.OnDownloadMoreAsnovaNewsUseCase
import com.asnova.model.Resource
import com.asnova.model.WallItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getSafetyNewsUseCase: GetSafetyNewsUseCase,
    private val onDownloadMoreAsnovaNewsUseCase: OnDownloadMoreAsnovaNewsUseCase,
    private val getAsnovaNewsUseCase: GetAsnovaNewsUseCase
) : ViewModel() {
    private val _state = mutableStateOf(FeedState())
    val state: State<FeedState> = _state

    private val _wallItems = MutableLiveData<List<WallItem>>()
    val wallItems: LiveData<List<WallItem>> get() = _wallItems

    private val _safetyWallItems = MutableLiveData<List<WallItem>>()
    val safetyWallItems: LiveData<List<WallItem>> get() = _safetyWallItems

    private val _downloadMore = MutableLiveData(true)
    val downloadMore: LiveData<Boolean> get() = _downloadMore


    init {
//        onUpdateWall(wallId = 162375388)
//        onUpdateWall(wallId = 80108699)
        loadAsnovaNews()
    }

    private fun loadAsnovaNews() {
        getAsnovaNewsUseCase(callback = { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = FeedState(value = result.data?.distinctBy { it.id }?.sortedByDescending { it.date }
                        ?: emptyList())
                }

                is Resource.Error -> {
                    _state.value =
                        FeedState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    _state.value = FeedState(loading = true)
                }
            }
        })
    }

    fun onDownloadMore() {
        val currentList = _state.value.value.toMutableList()

        onDownloadMoreAsnovaNewsUseCase(
            offset = currentList.size,
            callback = { result ->
                when (result) {
                    is Resource.Success -> {
                        val loadedData = result.data ?: emptyList()

                        _state.value = FeedState(
                            value = currentList.apply { addAll(loadedData) }
                                .distinctBy { it.id }
                                .sortedByDescending { it.date }
                        )
                        Log.e("vk_info", "onDownloadMore")
                    }

                    is Resource.Error -> {
                        _state.value = FeedState(error = result.message ?: "An unexpected onDownloadMore error occurred")
                    }

                    is Resource.Loading -> {
                        // _state.value = FeedState(loading = true)
                    }
                }
            })
    }

    fun pullToRefresh() = viewModelScope.launch {

    }
}
