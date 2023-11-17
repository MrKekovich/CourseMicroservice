package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.CourseEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * @property title Course name
 * @property description Course description
 */
sealed class CourseDto : BaseDto<CourseEntity, String>() {
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

    data class Response(
        override val title: String?,
        override val description: String?,

        @get:NotNull
        @get:NotBlank
        override val id: String?,
    ) : CourseDto() {
        constructor(courseEntity: CourseEntity) : this(
            title = courseEntity.title,
            description = courseEntity.description,
            id = courseEntity.id,
        )
    }

    data class Request(
        override val title: String?,
        override val description: String?
    ) : CourseDto()
}
