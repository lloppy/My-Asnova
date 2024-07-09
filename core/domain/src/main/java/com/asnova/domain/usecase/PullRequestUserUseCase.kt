package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.User

class PullRequestUserUseCase(
    private val userRepository: UserRepository
){
    operator fun invoke(callback: (Resource<User?>) -> Unit)
    {
        return userRepository.pullRequest(callback)
    }
}