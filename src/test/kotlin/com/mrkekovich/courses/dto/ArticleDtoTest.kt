package com.mrkekovich.courses.dto

import org.junit.jupiter.api.Test

class ArticleDtoTest {
    @Test
    fun `should validate length`() {
        // Length validation is defined in Parent class,
        // By validating Create request only we can validate all Article DTOs.

        // act
        validateLength(
            validLengthRange = 1 until 255,
            testRange = 0..300
        ) {
            CreateArticleRequest(
                title = it,
                description = "valid",
                content = "valid",
                moduleId = "valid",
            )
        }

        validateLength(
            validLengthRange = 1 until 1000,
            testRange = 1..1500,
        ) {
            CreateArticleRequest(
                title = "valid",
                description = it,
                content = "valid",
                moduleId = "valid",
            )
        }

        validateLength(
            validLengthRange = 1 until 300000,
            testRange = 1..300500,
        ) {
            CreateArticleRequest(
                title = "valid",
                description = "valid",
                content = it,
                moduleId = "valid",
            )
        }
    }

    @Test
    fun `create article request should validate`() {
        // arrange
        val createDto = { field: String? ->
            CreateArticleRequest(
                title = field,
                description = field,
                content = field,
                moduleId = field,
            )
        }

        val validDto = createDto("test")

        // act
        val violations =
            validator.validate(validDto)

        // assert
        assert(violations.isEmpty())
        validateNotBlank(1, createDto)
        validateNotNull(4, createDto)
    }

    @Test
    fun `update article request should validate`() {
        // arrange
        val updateDto = { field: String? ->
            UpdateArticleRequest(
                title = field,
                description = field,
                content = field,
                moduleId = field,
                id = field
            )
        }

        val validDto = updateDto("test")

        // act
        val violations =
            validator.validate(validDto)

        // assert
        assert(violations.isEmpty())
        validateNotBlank(2, updateDto)
        validateNotNull(4, updateDto)
    }

    @Test
    fun `delete article request should validate`() {
        // assert
        validateNotBlank(1) {
            DeleteArticleRequest(id = it)
        }
    }
}
