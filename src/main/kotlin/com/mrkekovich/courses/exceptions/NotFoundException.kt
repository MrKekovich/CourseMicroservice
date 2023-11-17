package com.mrkekovich.courses.exceptions

class NotFoundException(
    message: String? = null,
    val id: String? = null
) : RuntimeException(message)