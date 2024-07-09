package com.asnova.domain.usecase

import com.asnova.domain.repository.storage.IsAuthedUserStorage

class GetIsAuthedUserUseCase(
    private val isAuthedUserStorage: IsAuthedUserStorage
) {
    operator fun invoke() : String {
        return isAuthedUserStorage.get()
    }
}