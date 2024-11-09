package com.example.asnova.screen.schedule

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.CheckUserClassUseCase
import com.asnova.domain.usecase.GetScheduleFromSiteUseCase
import com.asnova.domain.usecase.GetScheduleMapUseCase
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
    private val getScheduleMapUseCase: GetScheduleMapUseCase,

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

    init {
        loadAvailableSchedule()
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
        Log.e("currentGroup", "до " + currentGroup.toString())

        getScheduleMapUseCase(callback = { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ScheduleState(loading = true)
                }

                is Resource.Success -> {
                    _state.value = ScheduleState(privateSchedule = result.data ?: emptyMap())
                        filterAndUpdateSchedules(currentGroup)
                }

                is Resource.Error -> {
                    _state.value = ScheduleState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
            }
        })
    }

    private fun filterAndUpdateSchedules(currentGroup: String?) {
        val filteredSchedules = _state.value.privateSchedule.values.flatten().filter { schedule ->
            currentGroup.isNullOrEmpty() || schedule.summary == currentGroup || schedule.trimmedSummary == currentGroup
        }

        val updatedMap = filteredSchedules.groupBy { it.start.toLocalDate() }
        _state.value = _state.value.copy(privateSchedule = updatedMap)
    }

    private fun loadScheduleFromSite() {
        getScheduleFromSiteUseCase(callback = { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ScheduleState(loading = true)
                }

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
            }
        })
    }

    fun saveDate(date: LocalDate) {
        selectedDateMutableState.value = date
    }

    fun checkUserClass(callback: (Resource<Boolean>) -> Unit) {
        checkUserClassUseCase.invoke(callback)
    }

    fun pullToRefresh() {
        if (UserManager.getRole() == Role.ADMIN) loadScheduleFromSite() else loadAvailableSchedule()
    }

}