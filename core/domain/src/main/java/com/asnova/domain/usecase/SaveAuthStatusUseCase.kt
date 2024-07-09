package com.asnova.domain.usecase

import com.asnova.domain.repository.storage.IsAuthedUserStorage

class SaveAuthStatusUseCase(
    private val isAuthedUserStorage: IsAuthedUserStorage
) {
    operator fun invoke(status: String) {
        return isAuthedUserStorage.save(status)
    }
}