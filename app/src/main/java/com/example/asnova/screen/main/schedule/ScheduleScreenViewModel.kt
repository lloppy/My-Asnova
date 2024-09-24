package com.example.asnova.screen.main.schedule

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.GetScheduleFromSiteUseCase
import com.asnova.domain.usecase.GetScheduleStateUseCase
import com.asnova.domain.usecase.GetScheduleUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.SaveScheduleStateUseCase
import com.asnova.model.AsnovaSchedule
import com.asnova.model.AsnovaSiteSchedule
import com.asnova.model.Resource
import com.asnova.model.Role
import com.asnova.model.User
import com.example.asnova.data.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleScreenViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getScheduleFromSiteUseCase: GetScheduleFromSiteUseCase,

    private val saveScheduleStateUseCase: SaveScheduleStateUseCase,
    private val getScheduleStateUseCase: GetScheduleStateUseCase,

    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _state = mutableStateOf(AsnovaScheduleState())
    val state: State<AsnovaScheduleState> = _state

    private val selectedDateMutableState = MutableLiveData(LocalDate.now())
    val selectedDate: MutableLiveData<LocalDate?> = selectedDateMutableState

    init {
        when (UserManager.getRole()) {
            Role.STUDENT, Role.WORKER, Role.ADMIN -> loadScheduleForGroup()
        }
        loadScheduleFromSite()
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

                    _state.value = AsnovaScheduleState(privateSchedule = result.data ?: emptyList())
                    val temp = mutableListOf<AsnovaSchedule>()
                    for (item in _state.value.privateSchedule) {
                        if (selectedDateMutableState.value?.dayOfMonth == item.start.dayOfMonth &&
                            selectedDateMutableState.value?.monthValue == item.start.monthValue &&
                            selectedDateMutableState.value?.year == item.start.year
                        ) {
                            temp.add(item)
                        }
                    }
                    _state.value.privateSchedule = temp
                }

                is Resource.Error -> {
                    _state.value = AsnovaScheduleState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _state.value = AsnovaScheduleState(loading = true)
                }
            }
        })
    }

    fun loadScheduleFromSite() {
        getScheduleFromSiteUseCase(callback = { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = AsnovaScheduleState(siteSchedule = result.data ?: emptyList())
                    val temp = mutableListOf<AsnovaSiteSchedule>()
                    for (item in _state.value.siteSchedule) {
                        temp.add(item)
                    }
                    _state.value.siteSchedule = temp
                }

                is Resource.Error -> {
                    _state.value = AsnovaScheduleState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _state.value = AsnovaScheduleState(loading = true)
                }
            }
        })
    }

    private fun filterScheduleBySelectedDate() {
        val temp = _state.value.privateSchedule.filter {
            selectedDateMutableState.value?.let { date ->
                it.start.toLocalDate() == date
            } ?: false
        }
        _state.value = _state.value.copy(privateSchedule = temp)
    }


    fun saveDate(date: LocalDate) {
        selectedDateMutableState.value = date
        filterScheduleBySelectedDate()
    }

    fun pullToRefresh() {
        when (UserManager.getRole()) {
            Role.STUDENT, Role.WORKER, Role.ADMIN -> loadScheduleForGroup()
        }
        loadScheduleFromSite()
    }
}