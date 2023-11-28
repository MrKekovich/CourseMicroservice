package com.mrkekovich.courses.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Used to send error messages.
 *
 * @property errorMessage Error message to show.
 * alias ([JsonProperty]) is "error_message".
 *
 * @property status Status code.
 */
data class ErrorDto(
    @JsonProperty("error_message")
    val errorMessage: String?,

    val status: Int?,
)
