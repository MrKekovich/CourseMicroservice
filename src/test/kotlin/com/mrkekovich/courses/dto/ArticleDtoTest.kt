package com.mrkekovich.courses.dto

import org.junit.jupiter.api.Test

class ArticleDtoTest : BaseTestDto() {
    @Test
    fun `article dto should validate title, description and content length`() {
        // Create dto was chosen because it has fields we need.

        // <editor-fold desc="Arrange">
        val validDto = ArticleDto.Request.Create(
            title = "less than 255",
            description = "less than 1000",
            content = "less than 300000",
        )
        val invalidTitle = ArticleDto.Request.Create(
            title = "more than 255".repeat(255),
            description = "less than 1000",
            content = "less than 300000",
        )
        val invalidDescription = ArticleDto.Request.Create(
            title = "less than 255",
            description = "more than 1000".repeat(1000),
            content = "less than 300000",
        )
        val invalidContent = ArticleDto.Request.Create(
            title = "less than 255",
            description = "less than 1000",
            content = "more than 300000".repeat(300000),
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validator.validate(validDto)

        val violationsInvalidTitle =
            validator.validate(invalidTitle)

        val violationsInvalidDescription =
            validator.validate(invalidDescription)

        val violationsInvalidContent =
            validator.validate(invalidContent)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsInvalidTitle.isNotEmpty())
        assert(violationsInvalidDescription.isNotEmpty())
        assert(violationsInvalidContent.isNotEmpty())
        // </editor-fold>
    }

    @Test
    fun `create request dto should validate title, description and content`() {
        // <editor-fold desc="Arrange">
        val validDto = ArticleDto.Request.Create(
            title = "Not blank",
            description = "Not blank",
            content = "Not blank",
        )
        val invalidTitle = ArticleDto.Request.Create(
            title = "",
            description = "Not blank",
            content = "Not blank",
        )
        val invalidDescription = ArticleDto.Request.Create(
            title = "Not blank",
            description = "",
            content = "Not blank",
        )
        val invalidContent = ArticleDto.Request.Create(
            title = "Not blank",
            description = "Not blank",
            content = "",
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val violationsValid =
            validator.validate(validDto)

        val violationsInvalidTitle =
            validator.validate(invalidTitle)

        val violationsInvalidDescription =
            validator.validate(invalidDescription)

        val violationsInvalidContent =
            validator.validate(invalidContent)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(violationsValid.isEmpty())
        assert(violationsInvalidTitle.isNotEmpty())
        assert(violationsInvalidDescription.isNotEmpty())
        assert(violationsInvalidContent.isNotEmpty())
        // </editor-fold>
    }

    @Test
    fun `update request dto should validate title, description and content`() {
        // <editor-fold desc="Arrange">
        val validDto = ArticleDto.Request.Update(
            title = "Not blank",
            description = "Not blank",
            content = "Not blank",
            moduleId = "Not blank",
            id = "Not blank",
        )
        val validNoModuleIdDto = ArticleDto.Request.Update(
            title = "Not blank",
            description = "Not blank",
            content = "Not blank",
            id = "Not blank",
            moduleId = "Not blank",
        )
    }
}
