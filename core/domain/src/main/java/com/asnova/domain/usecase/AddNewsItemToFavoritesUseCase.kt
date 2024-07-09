package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource

class AddNewsItemToFavoritesUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(id: String, callback: (Resource<Boolean>) -> Unit) {
        userRepository.addNewsItemToFavorites(id, callback)
    }
}