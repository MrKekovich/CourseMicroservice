package com.mrkekovich.coursemicroservice.exceptions

/**
 * Used to represent not found error.
 *
 * @param message Error message to show.
 */
class NotFoundException(
    message: String? = null
) : RuntimeException(message)
