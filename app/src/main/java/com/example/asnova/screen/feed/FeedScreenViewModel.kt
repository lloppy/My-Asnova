package com.example.asnova.screen.feed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(

) : ViewModel() {
    private val _state = mutableStateOf(FeedState())
    val state: State<FeedState> = _state

    fun pullToRefresh() {
        //TODO()
    }

}