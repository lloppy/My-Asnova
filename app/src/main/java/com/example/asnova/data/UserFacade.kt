package com.example.asnova.data


import android.content.Intent
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.SignInResult
import com.asnova.model.User
import javax.inject.Inject

class UserFacade @Inject constructor(
    private val userRepository: UserRepository
) {


}