package com.mrkekovich.courses.dto

import jakarta.validation.Validation
import org.junit.jupiter.api.Test

class CourseDtoTest {
    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.validator

    @Test
    fun `course dto should validate title and description length`() {
        // Create dto was chosen because it has fields we need.
        // Title and description are validated by length anyway.

        // <editor-fold desc="Arrange">
        val validDto = CourseDto.Request.Create(
            title = "less than 255",
            description = "less than 1000",
        )
        val invalidTitle = CourseDto.Request.Create(
            title = "more than 255".repeat(255),
            description = "less than 1000",
        )

        val invalidDescription = CourseDto.Request.Create(
            title = "less than 255",
            description = "more than 1000".repeat(1000),
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validator.validate(validDto)

        val violationsTitle =
            validator.validate(invalidTitle)

        val violationsDescription =
            validator.validate(invalidDescription)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsTitle.isNotEmpty())
        assert(violationsDescription.isNotEmpty())
        // </editor-fold>
    }

    @Test
    fun `create request course dto should validate`() {
        // <editor-fold desc="Arrange">
        val validDto = CourseDto.Request.Create(
            title = "title",
            description = "description",
        )
        val invalidBlankTitle = CourseDto.Request.Create(
            title = "",
            description = "description",
        )
        val invalidBlankDescription = CourseDto.Request.Create(
            title = "title",
            description = "",
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validator.validate(validDto)

        val violationsBlankTitle =
            validator.validate(invalidBlankTitle)

        val violationsBlankDescription =
            validator.validate(invalidBlankDescription)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsBlankTitle.isNotEmpty())
        assert(violationsBlankDescription.isNotEmpty())
        // </editor-fold>
    }

    @Test
    fun `get all request course dto should validate`() {
        // <editor-fold desc="Arrange">
        val validDtos = (-1..100).map { limit ->
            CourseDto.Request.GetAll(
                limit = limit
            )
        }

        val invalidDtos = (-2 downTo -100).map { limit ->
            CourseDto.Request.GetAll(
                limit = limit
            )
        }
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validDtos.map { validDto ->
                validator.validate(validDto)
            }

        val violationsDtos =
            invalidDtos.map { invalidDto ->
                validator.validate(invalidDto)
            }
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.all { it.isEmpty() })
//        assert(violationsZeroLimit.isNotEmpty()) TODO: Exclude zero
        assert(violationsDtos.all { it.isNotEmpty() })
        // </editor-fold>
    }

    @Test
    fun `update request course dto should validate`() {
        // <editor-fold desc="Arrange">
        val validDto = CourseDto.Request.Update(
            title = "title",
            description = "description",
            id = "id",
        )
        val invalidBlankTitle = CourseDto.Request.Update(
            title = "",
            description = "description",
            id = "id",
        )
        val invalidBlankDescription = CourseDto.Request.Update(
            title = "title",
            description = "",
            id = "id",
        )
        val invalidBlankId = CourseDto.Request.Update(
            title = "title",
            description = "description",
            id = "",
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validator.validate(validDto)

        val violationsBlankTitle =
            validator.validate(invalidBlankTitle)

        val violationsBlankDescription =
            validator.validate(invalidBlankDescription)

        val violationsBlankId =
            validator.validate(invalidBlankId)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsBlankTitle.isNotEmpty())
        assert(violationsBlankDescription.isNotEmpty())
        assert(violationsBlankId.isNotEmpty())
        // </editor-fold>
    }

    @Test
    fun `delete request course dto should validate`() {
        // <editor-fold desc="Arrange">
        val validDto = CourseDto.Request.Delete(
            id = "id",
        )
        val invalidBlankId = CourseDto.Request.Delete(
            id = "",
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validator.validate(validDto)

        val violationsBlankId =
            validator.validate(invalidBlankId)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsBlankId.isNotEmpty())
        // </editor-fold>
    }
}
