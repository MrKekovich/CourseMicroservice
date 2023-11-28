package com.mrkekovich.courses.handlers

import com.mrkekovich.courses.dto.ErrorDto
import com.mrkekovich.courses.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Used to handle global exceptions that may occur in the application.
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    /**
     * Used to handle [NotFoundException]
     * and return [ErrorDto] with [HttpStatus.NOT_FOUND] status
     * wrapped in [ResponseEntity].
     *
     * @param notFoundException [NotFoundException] occurred in the application.
     * @return [ResponseEntity] with [ErrorDto] and [HttpStatus.NOT_FOUND] status.
     */
    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(
        notFoundException: NotFoundException,
    ): ResponseEntity<ErrorDto> {
        return ResponseEntity(
            ErrorDto(
                errorMessage = notFoundException.message ?: "Record not found",
                status = HttpStatus.NOT_FOUND.value()
            ),
            HttpStatus.NOT_FOUND
        )
    }
}
