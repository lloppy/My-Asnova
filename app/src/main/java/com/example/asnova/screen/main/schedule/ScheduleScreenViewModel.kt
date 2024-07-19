package com.example.asnova.screen.main.schedule

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.GetScheduleStateUseCase
import com.asnova.domain.usecase.GetScheduleUseCase
import com.asnova.domain.usecase.SaveScheduleStateUseCase
import com.asnova.model.AsnovaSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import com.asnova.model.Resource
import com.asnova.model.Schedule
import javax.inject.Inject

@HiltViewModel
class ScheduleScreenViewModel @Inject constructor(
    private val saveScheduleStateUseCase: SaveScheduleStateUseCase,
    private val getScheduleStateUseCase: GetScheduleStateUseCase,
    private val getScheduleUseCase: GetScheduleUseCase
) : ViewModel() {

    private val _state = mutableStateOf(AsnovaScheduleState())
    val state: State<AsnovaScheduleState> = _state

    private val selectedDateMutableState = MutableLiveData(LocalDate.now())
    val selectedDate: MutableLiveData<LocalDate?> = selectedDateMutableState
    init {
        loadSchedule()
    }

    fun loadSchedule()
    {
        Log.d("calendar_info", "loadSchedule called")

        getScheduleUseCase(callback = { result ->
            when(result){
                is Resource.Success -> {
                    Log.d("calendar_info", "Schedules loaded")

                    _state.value = AsnovaScheduleState(value = result.data ?: emptyList())
                    val temp = mutableListOf<AsnovaSchedule>()
                    for (item in _state.value.value)
                    {
                        if (selectedDateMutableState.value?.dayOfMonth == item.start.dayOfMonth &&
                            selectedDateMutableState.value?.monthValue == item.start.monthValue &&
                            selectedDateMutableState.value?.year == item.start.year)
                        {
                            temp.add(item)
                        }
                    }
                    _state.value.value = temp
                }
                is Resource.Error -> {
                    Log.d("calendar_info", "Error loading schedules")
                    _state.value = AsnovaScheduleState(error = result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    Log.d("calendar_info", "Loading schedules")
                    _state.value = AsnovaScheduleState(loading = true)
                }
            }
        })
    }

    fun saveDate(date: LocalDate) {
        selectedDateMutableState.value = date
    }
    fun pullToRefresh() {
        loadSchedule()
    }
}