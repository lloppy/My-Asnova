package com.example.asnova.screen.settings.components.admin_classes.command

import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.CleanAsnovaClassesFromFirebaseUseCase
import com.asnova.domain.usecase.GetAsnovaClassesFromFirebaseUseCase
import com.asnova.domain.usecase.GetRawAsnovaClassesUseCase
import com.asnova.domain.usecase.PushAsnovaClassesUseCase
import javax.inject.Inject

class ClassesViewModel @Inject constructor(

    private val pushAsnovaClassesUseCase: PushAsnovaClassesUseCase,

    private val getAsnovaClassesFromFirebaseUseCase: GetAsnovaClassesFromFirebaseUseCase,
    private val getRawAsnovaClassesUseCase: GetRawAsnovaClassesUseCase,

    private val cleanAsnovaClassesFromFirebaseUseCase: CleanAsnovaClassesFromFirebaseUseCase

) : ViewModel(), CommandReceiver {

    override fun onAddClicked() {
        TODO("Not yet implemented")
    }

    override fun onDeleteClicked() {
        TODO("Not yet implemented")
    }

    override fun onDeleteAllClicked() {
        TODO("Not yet implemented")
    }

    override fun onGetRawClicked() {
        TODO("Not yet implemented")
    }

    override fun onGetFirebaseClicked() {
        TODO("Not yet implemented")
    }

    override fun onSaveClicked() {
        TODO("Not yet implemented")
    }


}