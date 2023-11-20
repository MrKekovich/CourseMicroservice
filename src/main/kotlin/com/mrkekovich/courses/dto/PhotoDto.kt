package com.mrkekovich.courses.dto

import com.mrkekovich.courses.annotations.AllowedExtensions
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

sealed class PhotoDto {
    sealed class Request {
        data class Upload(
            @get:NotNull
            @get:AllowedExtensions(["png", "jpg", "jpeg"])
            val file: MultipartFile,
        )

        data class Download(
            @get:NotNull
            @get:NotBlank
            val id: String
        )
    }
}
