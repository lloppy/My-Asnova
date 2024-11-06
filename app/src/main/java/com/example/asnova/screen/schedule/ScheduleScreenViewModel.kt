package com.example.asnova.screen.schedule

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.CheckUserClassUseCase
import com.asnova.domain.usecase.GetScheduleFromSiteUseCase
import com.asnova.domain.usecase.GetScheduleStateUseCase
import com.asnova.domain.usecase.GetScheduleUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.SaveScheduleStateUseCase
import com.asnova.model.Resource
import com.asnova.model.Role
import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite
import com.asnova.model.User
import com.example.asnova.data.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleScreenViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getScheduleFromSiteUseCase: GetScheduleFromSiteUseCase,
    private val checkUserClassUseCase: CheckUserClassUseCase,
    private val saveScheduleStateUseCase: SaveScheduleStateUseCase,
    private val getScheduleStateUseCase: GetScheduleStateUseCase,

    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ScheduleState())
    val state: State<ScheduleState> = _state

    private val selectedDateMutableState = MutableLiveData(LocalDate.now())
    val selectedDate: MutableLiveData<LocalDate?> = selectedDateMutableState

    fun checkUserClass(callback: (Resource<Boolean>) -> Unit) {
        checkUserClassUseCase.invoke(callback)
    }

    fun canLoadPrivateSchedule(): Boolean {
        return when (UserManager.getRole()) {
            Role.STUDENT, Role.WORKER, Role.ADMIN -> true
            Role.GUEST, Role.NONE -> false
            else -> false
        }
    }

    private fun loadAvailableSchedule() {
        when (UserManager.getRole()) {
            Role.STUDENT, Role.WORKER -> loadScheduleForGroup() // TODO () нужно передавать в параметры учителей и учеников параметр - группа, по которой нужно загрузить расписание и фильтровать по ней у админа такого фильтра просто не будет
            Role.GUEST, Role.NONE -> loadScheduleFromSite()
            Role.ADMIN -> {
                saveDate(LocalDate.now())
                loadScheduleForGroup()
            }
        }
    }

    fun getUserData(callback: (Resource<User?>) -> Unit) {
        getUserDataUseCase.invoke(callback)
    }

    fun loadScheduleForGroup(currentGroup: String? = "") {
        Log.d("calendar_info", "loadSchedule called")
        Log.e("currentGroup", "до " + currentGroup.toString())

        getScheduleUseCase(callback = { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ScheduleState(loading = true)
                }

                is Resource.Success -> {
                    Log.d("calendar_info", "Schedules loaded")

                    _state.value = ScheduleState(privateSchedule = result.data ?: emptyList())
                    val temp = mutableListOf<ScheduleAsnovaPrivate>()
                    for (item in _state.value.privateSchedule) {
                        if (selectedDateMutableState.value?.dayOfMonth == item.start.dayOfMonth &&
                            selectedDateMutableState.value?.monthValue == item.start.monthValue &&
                            selectedDateMutableState.value?.year == item.start.year
                        ) {
                            temp.add(item)
                        }
                    }

                    Log.e("currentGroup", "после " + currentGroup.toString())
                    if (!currentGroup.isNullOrEmpty()) {
                        val filteredTemp = temp.filter {
                            Log.e("currentGroup", "после currentGroup " +  it.trimmedSummary)
                            it.summary == currentGroup || it.trimmedSummary == currentGroup
                        }
                        _state.value.privateSchedule = filteredTemp

                    } else {
                        _state.value.privateSchedule = temp
                    }
                }

                is Resource.Error -> {
                    _state.value = ScheduleState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
            }
        })
    }

    private fun loadScheduleFromSite() {
        getScheduleFromSiteUseCase(callback = { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ScheduleState(siteSchedule = result.data ?: emptyList())
                    val temp = mutableListOf<ScheduleAsnovaSite>()
                    for (item in _state.value.siteSchedule) {
                        temp.add(item)
                    }
                    _state.value.siteSchedule = temp
                }

                is Resource.Error -> {
                    _state.value = ScheduleState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _state.value = ScheduleState(loading = false)
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

//        if (!currentGroup.isNullOrEmpty()) {
//            val filteredTemp = temp.filter {
//                Log.e("currentGroup", "после currentGroup " +  it.trimmedSummary)
//                it.summary == currentGroup || it.trimmedSummary == currentGroup
//            }
//            _state.value = _state.value.copy(privateSchedule = filteredTemp)
//
//        } else {
            _state.value = _state.value.copy(privateSchedule = temp)

    }


    fun saveDate(date: LocalDate) {
        selectedDateMutableState.value = date
        filterScheduleBySelectedDate()
    }

    fun pullToRefresh() {
        if (UserManager.getRole() == Role.ADMIN) loadScheduleFromSite() else loadAvailableSchedule()
    }
}