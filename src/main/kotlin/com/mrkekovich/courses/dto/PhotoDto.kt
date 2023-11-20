package com.mrkekovich.courses.dto

import com.mrkekovich.courses.annotations.AllowedExtensions
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

/**
 * Photo dto:
 * Used to transfer validated
 * data between client and server.
 *
 * @property id id of a photo.
 * @property fileName file name of a photo.
 */
sealed class PhotoDto {
    @get:Schema(description = "Photo id")
    open val id: String? = null

    @get:Schema(description = "Photo file name")
    open val fileName: String? = null

    /**
     * Response dto:
     * Sealed class that contains
     * DTOs for responses.
     *
     * @see [PhotoDto]
     */
    sealed class Response : PhotoDto() {
        /**
         * Base response:
         * Represents a base response from server
         *
         * @property id [Response.id]
         * @property fileName [Response.fileName]
         * @constructor Create empty Base
         */
        @Schema(name = "Base response")
        data class Base(
            override val id: String?,

            override val fileName: String?
        ) : Response()
    }

    /**
     * Request dto:
     * Sealed class that contains
     * DTOs for requests.
     *
     * @see [PhotoDto]
     */
    sealed class Request : PhotoDto() {

        /**
         * Upload request:
         * Represents a request to upload a photo.
         *
         * @property file MultipartFile representing a photo from client.
         * * [NotNull],
         * * [AllowedExtensions] (["png", "jpg", "jpeg"]).
         */
        @Schema(name = "Upload photo")
        data class Upload(
            @get:NotNull
            @get:AllowedExtensions(["png", "jpg", "jpeg"])
            val file: MultipartFile?,
        ) : Request()

        /**
         * Download request:
         * Represents a request to download a photo.
         *
         * @property id [Request.id]
         * * [NotNull],
         * * [NotBlank].
         */
        @Schema(name = "Download photo")
        data class Download(
            @get:NotNull
            @get:NotBlank
            override val id: String?
        ) : Request()
    }
}
