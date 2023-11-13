package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.CourseEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * Used to easily validate, convert, map and return data.
 * @property title Course name
 * @property description Course description
 * @property id (optional) Course ID
 */
sealed class CourseDto : BaseDto<CourseEntity>() {
    @get:Length(min = 2, max = 50)
    @get:NotNull
    @get:NotBlank
    abstract val title: String?

    @get:Length(max = 500)
    @get:NotNull
    @get:NotBlank
    abstract val description: String?

    override fun toEntity(): CourseEntity {
        return CourseEntity(
            title = title,
            description = description,
            id = id
        )
    }

    override fun toEntity(id: String?): CourseEntity {
        return CourseEntity(
            title = title,
            description = description,
            id = id
        )
    }

    /**
     * Used to represent requests from clients.
     * @property title Course title
     * @property description Course description
     */
    data class Request(
        override val title: String?,
        override val description: String?
    ) : CourseDto()

    /**
     * Used to represent responses from server.
     * Can be initialized with CourseEntity.
     * @property id ID of course.
     * @property title Title of course.
     * @property description Description of course.
     */
    data class Response(
        override val title: String?,
        override val description: String?,

        @NotNull
        @NotBlank
        override val id: String?,
    ) : CourseDto() {
        constructor(courseEntity: CourseEntity) : this(
            title = courseEntity.title,
            description = courseEntity.description,
            id = courseEntity.id,
        )
    }
}
