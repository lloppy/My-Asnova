package com.example.asnova.screen.main.schedule

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.GetScheduleStateUseCase
import com.asnova.domain.usecase.GetScheduleUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.SaveScheduleStateUseCase
import com.asnova.model.AsnovaSchedule
import com.asnova.model.Resource
import com.asnova.model.User
import com.example.asnova.screen.main.feed.components.ScheduleSegments
import com.example.asnova.screen.main.feed.components.Segments
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleScreenViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase,

    private val saveScheduleStateUseCase: SaveScheduleStateUseCase,
    private val getScheduleStateUseCase: GetScheduleStateUseCase,

    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _state = mutableStateOf(AsnovaScheduleState())
    val state: State<AsnovaScheduleState> = _state

    private val selectedDateMutableState = MutableLiveData(LocalDate.now())
    val selectedDate: MutableLiveData<LocalDate?> = selectedDateMutableState

    private var selectedSegment by mutableStateOf(ScheduleSegments.ALL)

    init {
        loadScheduleForGroup()
    }

    fun getUserData(callback: (Resource<User?>) -> Unit) {
        getUserDataUseCase.invoke(callback)
    }

    fun loadScheduleForGroup() {
        Log.d("calendar_info", "loadSchedule called")

        getScheduleUseCase(callback = { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("calendar_info", "Schedules loaded")

                    _state.value = AsnovaScheduleState(value = result.data ?: emptyList())
                    val temp = mutableListOf<AsnovaSchedule>()
                    for (item in _state.value.value) {
                        if (selectedDateMutableState.value?.dayOfMonth == item.start.dayOfMonth &&
                            selectedDateMutableState.value?.monthValue == item.start.monthValue &&
                            selectedDateMutableState.value?.year == item.start.year
                        ) {
                            temp.add(item)
                        }
                    }
                    _state.value.value = temp
                }

                is Resource.Error -> {
                    Log.d("calendar_info", "Error loading schedules")
                    _state.value = AsnovaScheduleState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    Log.d("calendar_info", "Loading schedules")
                    _state.value = AsnovaScheduleState(loading = true)
                }
            }
        })
    }

    fun onSegmentChange(segment: String) {
        loadScheduleForSegment(segment)
    }

    private fun loadScheduleForSegment(segment: String) {
        selectedSegment = segment
        when (segment) {
            ScheduleSegments.ALL -> loadScheduleFromSite()
            ScheduleSegments.MY_GROUP -> loadScheduleForGroup()
        }
    }

    private fun loadScheduleFromSite() {
        TODO("Not yet implemented")
    }

    fun saveDate(date: LocalDate) {
        selectedDateMutableState.value = date
    }

    fun pullToRefresh() {
        loadScheduleFromSite()
    }
}