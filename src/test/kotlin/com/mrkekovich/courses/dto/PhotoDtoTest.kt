package com.mrkekovich.courses.dto

import jakarta.validation.Validation
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile

class PhotoDtoTest {
    private val factory = Validation.buildDefaultValidatorFactory()
    private val validator = factory.validator

    @Test
    fun `upload request photo dto`() {
        // <editor-fold desc="Arrange">
        val validDto = PhotoDto.Request.Upload(
            file = MockMultipartFile("test", "test.png", "image/png", "test".toByteArray()),
        )
        val invalidExtensionDto = PhotoDto.Request.Upload(
            file = MockMultipartFile("test.txt", "Hello".toByteArray()),
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid = validator.validate(validDto)
        val violationsExtension = validator.validate(invalidExtensionDto)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsExtension.isNotEmpty())
        // </editor-fold>
    }

    @Test
    fun `download request photo dto`() {
        // <editor-fold desc="Arrange">
        val validDto = PhotoDto.Request.Download(
            id = "id"
        )
        val invalidBlankId = PhotoDto.Request.Download(
            id = ""
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid = validator.validate(validDto)
        val violationsBlankId = validator.validate(invalidBlankId)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsBlankId.isNotEmpty())
        // </editor-fold>
    }
}
