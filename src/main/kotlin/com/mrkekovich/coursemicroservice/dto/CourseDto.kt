package com.mrkekovich.coursemicroservice.dto

import com.fasterxml.jackson.annotation.JsonProperty
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

    @get:NotNull
    override val description: String?,
) : CourseDto()

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

    @get:NotNull
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

/**
 * Get courses request represents a client request to get courses.
 *
 * @property pageSize Limit of courses on a single page.
 * - [NotNull]
 * - [Min] (1)
 *
 * @property page (offset) Which page to get.
 * - [NotNull]
 * - [Min] (0)
 *
 * @property title Title filter.
 *
 * @property description Description filter.
 *
 * @property id ID filter.
 */
@Schema(
    name = "Get courses request",
    description = "Represents a client request to get courses."
)
data class GetCoursesRequest(
    @get:Schema(description = "Page number (offset)")
    @get:JsonProperty("page")
    @get:NotNull
    @get:Min(0)
    val page: Int? = 0,

    @get:Schema(description = "How many courses to return on a single page.")
    @get:JsonProperty("page_size")
    @get:NotNull
    @get:Min(1)
    val pageSize: Int? = 10,

    @get:Schema(description = "Title filter.")
    override val title: String? = null,

    @get:Schema(description = "Description filter.")
    override val description: String? = null,

    @get:Schema(description = "ID filter.")
    override val id: String? = null,
) : CourseDto()

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
