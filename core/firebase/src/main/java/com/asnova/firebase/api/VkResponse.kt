package com.asnova.firebase.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VkResponse<R>(
    @SerialName("response") val response: R? = null,
    @SerialName("error") val error: VkApiError? = null
)
