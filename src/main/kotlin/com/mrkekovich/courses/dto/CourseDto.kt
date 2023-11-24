package com.mrkekovich.courses.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * Course Data Transfer Object.
 * Used to transfer validated data between client and server.
 *
 * @property title Title of a course.
 * - [Length]: (max = 255).
 *
 * @property description Description of a course.
 * - [Length]: (max = 1000).
 *
 * @property id ID of a course.
 */
sealed class CourseDto {
    @get:Schema(description = "Title of a course.")
    @get:Length(max = 255)
    open val title: String? = null

    @get:Schema(description = "Description of a course.")
    @get:Length(max = 1000)
    open val description: String? = null

    @get:Schema(description = "ID of a course.")
    open val id: String? = null
}

/**
 * Base response DTO represents a response from server with course data.
 *
 * @property title [ArticleDto.title]
 * @property description [ArticleDto.description]
 * @property id [ArticleDto.id]
 */
@Schema(
    name = "Base course response",
    description = "Represents a response from server with course data."
)
data class BaseCourseResponse(
    override val title: String?,
    override val description: String?,
    override val id: String?,
) : CourseDto()

/**
 * Create request represents a client request to create a new course.
 *
 * @property title [CourseDto.title]
 * - [NotBlank]
 *
 * @property description [CourseDto.description]
 * - [NotBlank]
 */
@Schema(
    name = "Create course request",
    description = "Represents a client request to create a new course."
)
data class CreateCourseRequest(
    @get:NotBlank
    override val title: String?,

    @get:NotBlank
    override val description: String?,
) : CourseDto()

/**
 * Get all request represents a client request to get all courses.
 *
 * @property limit Limit of courses to get (-1 means no limit).
 * - [Min] (-1).
 */
@Schema(
    name = "Get all courses request",
    description = "Represents a client request to get all courses."
)
data class GetAllCoursesRequest(
    @get:Schema(description = "Limit of courses to get. -1 means no limit.")
    @get:NotNull
    @get:Min(-1)
    val limit: Int? = -1,
)

/**
 * Update DTO represents a client request to update a course.
 * Contains new values for a course:
 *
 * @property title [CourseDto.title]
 * - [NotBlank]
 *
 * @property description [CourseDto.description]
 * - [NotBlank]
 *
 * @property id ID of a course to update.
 * - [NotBlank]
 */
@Schema(
    name = "Update course request",
    description = "Represents a client request to update a course."
)
data class UpdateCourseRequest(
    @get:NotBlank
    override val title: String?,

    @get:NotBlank
    override val description: String?,

    @get:NotBlank
    override val id: String?,
) : CourseDto()

/**
 * Delete DTO represents a client request to delete a course.
 *
 * @property id ID of a course to delete.
 * Validation:
 * - [NotBlank]
 */
@Schema(
    name = "Delete course request",
    description = "Represents a client request to delete a course."
)
data class DeleteCourseRequest(
    @get:NotBlank
    override val id: String?,
) : CourseDto()
