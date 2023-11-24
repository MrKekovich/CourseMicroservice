package com.mrkekovich.courses.dto

import org.junit.jupiter.api.Test

class ArticleDtoTest {

    @Test
    fun `should validate title length`() {
        validateLength(
            validLengthRange = 1..255,
            testRange = 0..300
        ) {
            CreateArticleRequest(
                title = it,
                description = "valid",
                content = "valid",
                moduleId = "valid"
            )
        }
    }

    @Test
    fun `should validate description length`() {
        validateLength(
            validLengthRange = 1..1000,
            testRange = 0..2000
        ) {
            CreateArticleRequest(
                title = "valid",
                description = it,
                content = "valid",
                moduleId = "valid"
            )
        }
    }

    @Test
    fun `should validate content length`() {
        validateLength(
            validLengthRange = 1..300000,
            testRange = 0..400000
        ) {
            CreateArticleRequest(
                title = "valid",
                description = "valid",
                content = it,
                moduleId = "valid"
            )
        }
    }
}
