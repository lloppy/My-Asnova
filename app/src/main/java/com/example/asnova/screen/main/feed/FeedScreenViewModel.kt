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

    private val _downloadMore = MutableLiveData(true)
    val downloadMore: LiveData<Boolean> get() = _downloadMore


    init {
        loadAsnovaNews()
        loadSafetyNews()
    }

    private fun loadAsnovaNews() {
        getAsnovaNewsUseCase(callback = { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = FeedState(asnovaNews = result.data?.distinctBy { it.id }
                        ?.sortedByDescending { it.date }
                        ?: emptyList())
                    Log.e("vk_info", "asnovaNews size is " + _state.value.asnovaNews.size)
                }

                is Resource.Error -> {
                    _state.value = FeedState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    _state.value = FeedState(loading = true)
                }
            }
        })
    }

    private fun loadSafetyNews() {
        getSafetyNewsUseCase(callback = { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = FeedState(safetyNews = result.data?.distinctBy { it.id }
                        ?.sortedByDescending { it.date }
                        ?: emptyList())
                    Log.e("vk_info", "safetyNews size is " + _state.value.safetyNews.size)
                }

                is Resource.Error -> {
                    _state.value = FeedState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    _state.value = FeedState(loading = true)
                }
            }
        })
    }

    fun onDownloadMore(selectedThreeSegment: String) {
        val currentList = when (selectedThreeSegment) {
            "Asnovapro" -> _state.value.asnovaNews.toMutableList()
            "Охрана труда" -> _state.value.safetyNews.toMutableList()
            else -> _state.value.asnovaNews.toMutableList()
        }


        onDownloadMoreAsnovaNewsUseCase(
            offset = currentList.size,
            callback = { result ->
                when (result) {
                    is Resource.Success -> {
                        val loadedData = result.data ?: emptyList()

                        _state.value = when (selectedThreeSegment) {
                            "Asnovapro" -> FeedState(
                                asnovaNews = currentList.apply { addAll(loadedData) }
                                    .distinctBy { it.id }
                                    .sortedByDescending { it.date }
                            )

                            "Охрана труда" -> FeedState(
                                safetyNews = currentList.apply { addAll(loadedData) }
                                    .distinctBy { it.id }
                                    .sortedByDescending { it.date }
                            )

                            else -> FeedState(
                                asnovaNews = currentList.apply { addAll(loadedData) }
                                    .distinctBy { it.id }
                                    .sortedByDescending { it.date }
                            )
                        }

                        Log.e("vk_info", "onDownloadMore")
                    }

                    is Resource.Error -> {
                        _state.value = FeedState(
                            error = result.message ?: "An unexpected onDownloadMore error occurred"
                        )
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
