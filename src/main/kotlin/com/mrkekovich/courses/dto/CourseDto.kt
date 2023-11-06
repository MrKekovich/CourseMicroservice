package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.CourseEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length


sealed class CourseDto(

    @Length(min = 2, max = 50)
    @NotNull
    @NotBlank
    open val name: String?,

    @Length(max = 500)
    @NotNull
    @NotBlank
    open val description: String?,

    open val id: String? = null,
) {
    fun toEntity(): CourseEntity {
        return CourseEntity(
            name = name,
            description = description,
            id = id
        )
    }

    data class Request(
        override val name: String?,
        override val description: String?
    ) : CourseDto(name, description) {
        constructor(courseEntity: CourseEntity) : this(
            name = courseEntity.name,
            description = courseEntity.description,
        )
    }

    data class Response(
        @NotNull
        @NotBlank
        override val id: String?,

        override val name: String?,
        override val description: String?
    ) : CourseDto(name, description) {
        constructor(courseEntity: CourseEntity) : this(
            name = courseEntity.name,
            description = courseEntity.description,
            id = courseEntity.id,
        )
    }
}
