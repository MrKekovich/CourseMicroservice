package com.mrkekovich.courses.dto

import org.junit.jupiter.api.Test

internal class ArticleDtoTest {
    @Test
    fun `should validate length`() {
        // Length validation is defined in Parent class,
        // By validating Create request only we can validate all Article DTOs.
        // act
        validateLength(
            maxLength = 255
        ) {
            CreateArticleRequest(
                title = it,
                description = "valid",
                content = "valid",
                moduleId = "valid",
            )
        }

        validateLength(
            maxLength = 1000
        ) {
            CreateArticleRequest(
                title = "valid",
                description = it,
                content = "valid",
                moduleId = "valid",
            )
        }

        validateLength(
            maxLength = 300000
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
        validateNotBlankString(2, createDto)
        validateNotNull(2, createDto)
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
        validateNotBlankString(3, updateDto)
        validateNotNull(3, updateDto)
    }

    @Test
    fun `delete article request should validate`() {
        // assert
        validateNotBlankString(1, ::DeleteArticleRequest)
        validateNotNull(1, ::DeleteArticleRequest)
    }

    @Test
    fun `get all articles request should validate`() {
        // arrange
        val validDto = GetAllArticlesRequest(
            limit = 1
        )
        val invalidDtos = (-100 until -1).map {
            GetAllArticlesRequest(limit = it)
        }

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
        validateNotNull(1, ::GetAllArticlesRequest)
    }
}
