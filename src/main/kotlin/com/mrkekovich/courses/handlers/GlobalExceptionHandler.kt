package com.mrkekovich.courses.handlers

import com.mrkekovich.courses.dto.ErrorDto
import com.mrkekovich.courses.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
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
