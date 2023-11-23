package com.mrkekovich.courses.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * Article dto:
 *
 * Contains [ArticleDto.Request] and [ArticleDto.Response]
 *
 * @property title Title of the article.
 * Validation:
 * - [Length]: (max = 255)
 *
 * @property description Description of the article.
 * Validation:
 * - [Length]: (max = 1000)
 *
 * @property content Content of the article.
 * Validation:
 * - [Length]: (max = 300000)
 *
 * @property moduleId ID of the module the article belongs to.
 * - [JsonProperty]: "module_id"
 *
 * @property id ID of the article.
 */
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

    @get:Schema(description = "The module article belongs to")
    @get:JsonProperty("module_id")
    open val moduleId: String? = null

    @get:Schema(description = "Article id")
    open val id: String? = null

    /**
     * Article request dto:
     * Contains [ArticleDto.Request.Create],
     * [ArticleDto.Request.Update]
     * and [ArticleDto.Request.Delete].
     *
     * @see [ArticleDto]
     */
    sealed class Request : ArticleDto() {
        /**
         * Create article request:
         *
         * @property title [ArticleDto.title]
         * Validation:
         * - [NotNull],
         * - [NotBlank]
         *
         * @property description [ArticleDto.description]
         * Validation:
         * - [NotNull]
         *
         * @property content [ArticleDto.content]
         * Validation:
         * - [NotNull]
         */
        @Schema(name = "Create article request")
        data class Create(
            @get:NotNull
            @get:NotBlank
            override val title: String?,

            @get:NotNull
            override val description: String?,

            @get:NotNull
            override val content: String?,
        ) : Request()

        /**
         * Update article request:
         *
         * @property title [ArticleDto.title]
         * Validation:
         * - [NotNull]
         * - [NotBlank]
         *
         * @property description [ArticleDto.description]
         * Validation:
         * - [NotNull].
         *
         * @property content [ArticleDto.content]
         * Validation:
         * - [NotNull].
         *
         * @property moduleId [ArticleDto.moduleId]
         * Validation:
         * - [NotNull]
         *
         * @property id [ArticleDto.id]
         * Validation:
         * - [NotNull]
         * - [NotBlank]
         */
        @Schema(name = "Update article request")
        data class Update(
            @get:NotNull
            @get:NotBlank
            override val title: String?,

            @get:NotNull
            override val description: String?,

            @get:NotNull
            override val content: String?,

            @get:NotNull
            override val moduleId: String? = null,

            @get:NotNull
            @get:NotBlank
            override val id: String?,
        ) : Request()

        /**
         * Delete article request:
         *
         * @property id [ArticleDto.id]
         * Validation:
         * - [NotNull]
         */
        @Schema(name = "Delete article request")
        data class Delete(
            @get:NotNull
            @get:NotBlank
            override val id: String?,
        ) : Request()

        /**
         * Get all articles request:
         *
         * @property limit Limit of article records to return.
         * -1 means no limit.
         * Validation:
         * - [NotNull]
         * - [Min]: (-1)
         */
        @Schema(name = "Get all articles request")
        data class GetAll(
            @Schema(description = "Article records limit. -1 means no limit.")
            @get:NotNull
            @get:Min(-1)
            val limit: Int = -1
        )
    }

    /**
     * Article response dto:
     * Contains [ArticleDto.Response.Base].
     *
     * @see [ArticleDto]
     */
    sealed class Response : ArticleDto() {
        /**
         * Base article response:
         *
         * @property id [ArticleDto.id]
         * @property title [ArticleDto.title]
         * @property description [ArticleDto.description]
         * @property content [ArticleDto.content]
         * @property moduleId [ArticleDto.moduleId]
         */
        @Schema(name = "Base article response")
        data class Base(
            override val id: String?,
            override val title: String?,
            override val description: String?,
            override val content: String?,
            override val moduleId: String?
        ) : Response()
    }
}
