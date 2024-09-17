package com.example.asnova.screen.main.feed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asnova.domain.usecase.GetAsnovaNewsUseCase
import com.asnova.domain.usecase.GetSafetyNewsUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.OnDownloadMoreAsnovaNewsUseCase
import com.asnova.domain.usecase.OnDownloadMoreSafetyNewsUseCase
import com.asnova.model.Resource
import com.asnova.model.User
import com.asnova.model.WallItem
import com.example.asnova.screen.main.feed.components.Segments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getAsnovaNewsUseCase: GetAsnovaNewsUseCase,
    private val getSafetyNewsUseCase: GetSafetyNewsUseCase,

    private val onDownloadMoreAsnovaNewsUseCase: OnDownloadMoreAsnovaNewsUseCase,
    private val onDownloadMoreSafetyNewsUseCase: OnDownloadMoreSafetyNewsUseCase,

    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _state = mutableStateOf(FeedState())
    val state: State<FeedState> = _state

    private var selectedSegment by mutableStateOf(Segments.WORK_PROFESSIONS)

    init {
        loadNewsForSegment(selectedSegment)
    }

    fun getUserData(callback: (Resource<User?>) -> Unit) {
        getUserDataUseCase.invoke(callback)
    }

    private fun loadNewsForSegment(segment: String) {
        selectedSegment = segment
        when (segment) {
            Segments.WORK_PROFESSIONS -> loadAsnovaNews()
            Segments.SAFETY -> loadSafetyNews()
        }
    }

    private fun loadAsnovaNews() {
        getAsnovaNewsUseCase(callback = { result ->
            handleNewsResult(result)
        })
    }

    private fun loadSafetyNews() {
        getSafetyNewsUseCase(callback = { result ->
            handleNewsResult(result)
        })
    }

    private fun handleNewsResult(result: Resource<List<WallItem>>) {
        when (result) {
            is Resource.Success -> {
                _state.value = FeedState(news = result.data?.distinctBy { it.id }
                    ?.sortedByDescending { it.date }
                    ?: emptyList())
            }

            is Resource.Error -> {
                _state.value = FeedState(error = result.message ?: "Ошибка. Проверьте интернет-соединение")
            }

            is Resource.Loading -> {
                _state.value = FeedState(loading = true)
            }
        }
    }

    fun onSegmentChange(segment: String) {
        loadNewsForSegment(segment)
    }

    fun onDownloadMore() {
        val currentList = _state.value.news.toMutableList()
        when (selectedSegment) {
            Segments.WORK_PROFESSIONS -> onDownloadMoreAsnovaNewsUseCase(
                offset = currentList.size,
                callback = { result ->
                    handleDownloadMoreResult(result, currentList)
                }
            )

            Segments.SAFETY -> onDownloadMoreSafetyNewsUseCase(
                offset = currentList.size,
                callback = { result ->
                    handleDownloadMoreResult(result, currentList)
                }
            )
        }
    }

    private fun handleDownloadMoreResult(
        result: Resource<List<WallItem>>,
        currentList: MutableList<WallItem>
    ) {
        when (result) {
            is Resource.Success -> {
                val loadedData = result.data ?: emptyList()
                _state.value = FeedState(
                    news = currentList.apply { addAll(loadedData) }
                        .distinctBy { it.id }
                        .sortedByDescending { it.date }
                )
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
    }

    fun pullToRefresh() = viewModelScope.launch {
        loadNewsForSegment(selectedSegment)
    }
}