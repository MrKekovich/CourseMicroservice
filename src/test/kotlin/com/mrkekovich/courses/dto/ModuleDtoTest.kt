package com.mrkekovich.courses.dto

import jakarta.validation.Validation
import org.junit.jupiter.api.Test

class ModuleDtoTest {
    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.validator

    @Test
    fun `module dto should validate title and description length`() {
        // Create dto was chosen because it has fields we need.

        // <editor-fold desc="Arrange">
        val validDto = ModuleDto.Request.Create(
            title = "less than 255",
            description = "less than 1000",
            courseId = "course",
        )

        val invalidTitle = ModuleDto.Request.Create(
            title = "more than 255".repeat(255),
            description = "less than 1000",
            courseId = "course",
        )

        val invalidDescription = ModuleDto.Request.Create(
            title = "less than 255",
            description = "more than 1000".repeat(1000),
            courseId = "course",
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
    fun `create request module dto should validate`() {
        // <editor-fold desc="Arrange">
        val validDto = ModuleDto.Request.Create(
            title = "title",
            description = "description",
            parentId = "parent",
            courseId = "course",
            order = 0,
        )
        val validDtoNoParentId = ModuleDto.Request.Create(
            title = "title",
            description = "description",
            courseId = "course",
            order = 0,
        )
        val validDtoNoOrder = ModuleDto.Request.Create(
            title = "title",
            description = "description",
            parentId = "parent",
            courseId = "course",
        )
        val invalidDtoBlankCourseId = ModuleDto.Request.Create(
            title = "title",
            description = "description",
            courseId = "",
        )
        val invalidBlankTitle = ModuleDto.Request.Create(
            title = "",
            description = "description",
            courseId = "course",
        )
        val invalidBlankDescription = ModuleDto.Request.Create(
            title = "title",
            description = "",
            courseId = "course",
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validator.validate(validDto)

        val violationsValidNoParentId =
            validator.validate(validDtoNoParentId)

        val violationsValidNoOrder =
            validator.validate(validDtoNoOrder)

        val violationsBlankCourseId =
            validator.validate(invalidDtoBlankCourseId)

        val violationsBlankTitle =
            validator.validate(invalidBlankTitle)

        val violationsBlankDescription =
            validator.validate(invalidBlankDescription)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsValidNoParentId.isEmpty())
        assert(violationsValidNoOrder.isEmpty())
        assert(violationsBlankCourseId.isNotEmpty())
        assert(violationsBlankTitle.isNotEmpty())
        assert(violationsBlankDescription.isNotEmpty())
        // </editor-fold>
    }

    @Test
    @Suppress("LongMethod")
    fun `update request module dto should validate`() {
        // <editor-fold desc="Arrange">
        val validDto = ModuleDto.Request.Update(
            title = "title",
            description = "description",
            parentId = "parent",
            courseId = "course",
            order = 0,
            id = "id",
        )
        val validNoParentId = ModuleDto.Request.Update(
            title = "title",
            description = "description",
            courseId = "course",
            order = 0,
            id = "id",
        )
        val validNoOrder = ModuleDto.Request.Update(
            title = "title",
            description = "description",
            courseId = "course",
            id = "id",
        )
        val invalidBlankTitle = ModuleDto.Request.Update(
            title = "",
            description = "description",
            courseId = "course",
            id = "id",
        )
        val invalidBlankDescription = ModuleDto.Request.Update(
            title = "title",
            description = "",
            courseId = "course",
            id = "id",
        )
        val invalidBlankCourseId = ModuleDto.Request.Update(
            title = "title",
            description = "description",
            courseId = "",
            id = "id",
        )
        val invalidBlankId = ModuleDto.Request.Update(
            title = "title",
            description = "description",
            courseId = "course",
            id = "",
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validator.validate(validDto)

        val violationsValidNoParentId =
            validator.validate(validNoParentId)

        val violationsValidNoOrder =
            validator.validate(validNoOrder)

        val violationsBlankTitle =
            validator.validate(invalidBlankTitle)

        val violationsBlankDescription =
            validator.validate(invalidBlankDescription)

        val violationsBlankCourseId =
            validator.validate(invalidBlankCourseId)

        val violationsBlankId =
            validator.validate(invalidBlankId)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsValidNoParentId.isEmpty())
        assert(violationsValidNoOrder.isEmpty())
        assert(violationsBlankTitle.isNotEmpty())
        assert(violationsBlankDescription.isNotEmpty())
        assert(violationsBlankCourseId.isNotEmpty())
        assert(violationsBlankId.isNotEmpty())
        // </editor-fold>
    }

    @Test
    fun `delete request module dto should validate`() {
        // <editor-fold desc="Arrange">
        val validDto = ModuleDto.Request.Delete(
            id = "id",
        )
        val invalidBlankId = ModuleDto.Request.Delete(
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
