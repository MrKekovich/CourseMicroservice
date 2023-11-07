package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.CourseEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * Course dto
 *
 * Sealed class for course DTOs.
 * Used to easily validate, convert, map and return data.
 *
 * @property name Course name
 * @property description Course description
 * @property id (optional) Course ID
 */
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

    /**
     * To entity
     *
     * Converts DTO to an entity.
     *
     * @return CourseEntity with randomly initialized ID.
     */
    fun toEntity(): CourseEntity {
        return CourseEntity(
            name = name,
            description = description,
            id = id
        )
    }

    /**
     * To entity with ID
     *
     * Used to create entity with given ID.
     *
     * @param id the ID of entity. Leave empty to generate new ID.
     * @return CourseEntity with given ID.
     */
    fun toEntity(id: String?): CourseEntity {
        return CourseEntity(
            name = name,
            description = description,
            id = id
        )
    }

    /**
     * Request data class.
     *
     * Used to represent requests from clients.
     * Can be initialized with CourseEntity or with name and description.
     *
     * @property name Course name
     * @property description Course description
     */
    data class Request(
        override val name: String?,
        override val description: String?
    ) : CourseDto(name, description) {
        constructor(courseEntity: CourseEntity) : this(
            name = courseEntity.name,
            description = courseEntity.description,
        )
    }

    /**
     * Response data class.
     *
     * Used to represent responses from server.
     *
     * @property id ID of course.
     * @property name Name of course.
     * @property description Description of course.
     */
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
