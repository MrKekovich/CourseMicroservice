package com.mrkekovich.courses.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

sealed class ArticleDto {
    @get:Schema(description = "Article title")
    @get:Length(max = 255)
    open val title: String? = null

    @get:Schema(description = "Article description")
    @get:Length(max = 1000)
    open val description: String? = null

    @get:Schema(description = "Article content")
    @get:Length(max = 300000)
    open val content: String? = null

    @get:Schema(description = "Parent module id")
    @get:JsonProperty("module_id")
    open val moduleId: String? = null

    @get:Schema(description = "Article id")
    open val id: String? = null

    sealed class Request : ArticleDto() {
        data class Create(
            @get:NotNull
            @get:NotBlank
            override val title: String?,

            @get:NotNull
            override val description: String?,

            @get:NotNull
            override val content: String?,
        ) : Request()

        data class Update(
            @get:NotNull
            @get:NotBlank
            override val title: String?,

            @get:NotNull
            @get:NotBlank
            override val description: String?,

            @get:NotNull
            @get:NotBlank
            override val content: String?,

            @get:NotNull
            @get:NotBlank
            override val id: String?,

            override val moduleId: String? = null
        ) : Request()

        data class Delete(
            @get:NotNull
            @get:NotBlank
            override val id: String?,
        ) : Request()

        data class GetAll(
            @Schema(description = "Article records limit")
            @get:NotNull
            @get:Min(-1)
            val limit: Int = -1
        )
    }

    sealed class Response : ArticleDto() {
        data class Base(
            override val id: String?,
            override val title: String?,
            override val description: String?,
            override val content: String?,
            override val moduleId: String?
        ) : Response()
    }
}
