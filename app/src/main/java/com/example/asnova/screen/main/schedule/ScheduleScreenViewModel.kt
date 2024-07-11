package com.example.asnova.screen.main.schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleScreenViewModel @Inject constructor(

) : ViewModel() {
    // https://calendar.mail.ru/principals/vk.com/ankudinovazaecologiy/calendars/e44497c4-4978-4518-81de-0530cf40c794/

    private val _state = mutableStateOf(ScheduleState())
    val state: State<ScheduleState> = _state

    private val selectedDateMutableState = MutableLiveData(LocalDate.now())
    val selectedDate: MutableLiveData<LocalDate?> = selectedDateMutableState
    init {
        loadSchedule()
    }

    fun loadSchedule() {

    }
    fun saveDate(date: LocalDate) {
        selectedDateMutableState.value = date
    }
    fun pullToRefresh() {
        loadSchedule()
    }
}