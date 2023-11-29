package com.mrkekovich.courses.dto

import org.junit.jupiter.api.Test

internal class CourseDtoTest {
    @Test
    fun `should validate length`() {
        // Length validation is defined in Parent class,
        // By validating Create request only we can validate all Article DTOs.

        validateLength(
            maxLength = 255,
        ) {
            CreateCourseRequest(
                title = it,
                description = "valid"
            )
        }
        validateLength(
            maxLength = 1000,
        ) {
            CreateCourseRequest(
                title = "valid",
                description = it
            )
        }
    }

    @Test
    fun `create course request should validate`() {
        // arrange
        val createDto = { field: String? ->
            CreateCourseRequest(
                title = field,
                description = field,
            )
        }
        val validDto = createDto("test")

        // act
        val violations =
            validator.validate(validDto)

        // assert
        assert(violations.isEmpty())
        validateNotBlankString(1, createDto)
        validateNotNull(2, createDto)
    }

    @Test
    fun `update course request should validate`() {
        // arrange
        val updateDto = { field: String? ->
            UpdateCourseRequest(
                title = field,
                description = field,
                id = field
            )
        }
        val validDto = updateDto("test")

        // act
        val violations =
            validator.validate(validDto)

        // assert
        assert(violations.isEmpty())
        validateNotBlankString(2, updateDto)
        validateNotNull(3, updateDto)
    }

    @Test
    fun `delete course request should validate`() {
        // assert
        validateNotBlankString(1, ::DeleteCourseRequest)
        validateNotNull(1, ::DeleteCourseRequest)
    }

    @Test
    fun `get courses request should validate`() {
        // arrange
        val validDto = GetCoursesRequest(
            pageSize = 1,
            page = 0
        )
        val invalidPageSizeDtos = (-100..0).map {
            GetCoursesRequest(pageSize = it, page = 0)
        }
        val invalidPageDtos = (-100..-1).map {
            GetCoursesRequest(pageSize = 1, page = it)
        }
        val invalidDtos = invalidPageSizeDtos + invalidPageDtos

        // act
        val violationsValid =
            validator.validate(validDto)

        val violationsInvalid =
            invalidDtos.map {
                validator.validate(it)
            }

        // assert
        assert(violationsValid.isEmpty())
        assert(violationsInvalid.all { it.isNotEmpty() })
        validateNotNull<Int>(2) { GetCoursesRequest(pageSize = it, page = it) }
    }
}
