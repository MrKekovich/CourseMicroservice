package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.CourseEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * Course DTO:
 * Used to transfer validated
 * data between client and server.
 *
 * @property title Title of a course.
 * * [Length] (min = 1, max = 255).
 *
 * @property description Description of a course.
 * * [Length] (min = 1, max = 1000).
 *
 * @property id ID of a course.
 */
sealed class CourseDto {
    @Schema(description = "Title of a course.")
    @Length(min = 1, max = 255)
    open val title: String? = null

    @Schema(description = "Description of a course.")
    @Length(min = 1, max = 1000)
    open val description: String? = null

    @Schema(description = "ID of a course.")
    open val id: String? = null

    /**
     * Response DTO:
     * Sealed class that contains
     * DTOs for responses.
     * @see [CourseDto]
     */
    sealed class Response : CourseDto() {
        /**
         * Base response:
         * Represents a response from server
         * with course data.
         *
         * @property title [Response.title]
         * @property description [Response.description]
         * @property id [Response.id]
         */
        @Schema(
            name = "Base response",
            description = "Represents a response from server with course data."
        )
        data class Base(
            override val title: String?,
            override val description: String?,
            override val id: String?,
        ) : Response() {
            constructor(entity: CourseEntity) : this(
                title = entity.title,
                description = entity.description,
                id = entity.id
            )
        }
    }

    /**
     * Request:
     * Sealed class that contains
     * DTOs for requests.
     * @see [CourseDto]
     */
    sealed class Request : CourseDto() {
        /**
         * Create request:
         * Represents a request to create a new course.
         *
         * @property title [Request.title]
         * * [NotNull]
         * * [NotBlank]
         *
         * @property description [Request.description]
         * * [NotNull]
         * * [NotBlank].
         */
        @Schema(
            name = "Create request",
            description = "Represents a request to create a new course."
        )
        data class Create(
            @NotNull
            @NotBlank
            override val title: String,

            @NotNull
            @NotBlank
            override val description: String,
        ) : Request()

        /**
         * Get all request:
         * Represents a request to get all courses.
         *
         * @property limit Limit of courses to get.
         * -1 means no limit.
         * * [Min] (-1).
         */
        @Schema(
            name = "Get all request",
            description = "Represents a request to get all courses."
        )
        data class GetAll(
            @Schema(description = "Limit of courses to get. -1 means no limit.")
            @Min(-1)
            val limit: Int = -1,
        )

        /**
         * Update request:
         * Represents a request to update a course.
         *
         * @property title [Request.title]
         * * [NotNull],
         * * [NotBlank].
         *
         * @property description [Request.description]
         * * [NotNull],
         * * [NotBlank].
         *
         * @property id [Request.id]
         * * [NotNull],
         * * [NotBlank].
         */
        @Schema(
            name = "Update request",
            description = "Represents a request to update a course."
        )
        data class Update(
            @NotNull
            @NotBlank
            override val title: String,

            @NotNull
            @NotBlank
            override val description: String,

            @NotNull
            @NotBlank
            override val id: String,
        ) : Request()

        /**
         * Delete request:
         * Represents a request to delete a course.
         * @property id [Request.id]
         */
        @Schema(
            name = "Delete request",
            description = "Represents a request to delete a course."
        )
        data class Delete(
            @NotNull
            @NotBlank
            override val id: String,
        ) : Request()
    }
}
