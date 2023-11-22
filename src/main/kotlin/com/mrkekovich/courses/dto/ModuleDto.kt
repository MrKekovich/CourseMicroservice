package com.mrkekovich.courses.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

sealed class ModuleDto {
    @get:Schema(description = "Module title")
    @get:Length(max = 255)
    open val title: String? = null

    @get:Schema(description = "Module description")
    @get:Length(max = 1000)
    open val description: String? = null

    @get:Schema(description = "Parent module id")
    open val parentId: String? = null

    @get:Schema(description = "Parent course id")
    open val courseId: String? = null

    @get:Schema(description = "Module order")
    open val order: Int? = null

    @get:Schema(description = "Module id")
    open val id: String? = null

    sealed class Request : ModuleDto() {
        @Schema(description = "Module creation request")
        data class Create(
            @get:NotBlank
            @get:NotNull
            override val title: String?,

            @get:NotBlank
            @get:NotNull
            override val description: String?,

            override val parentId: String? = null,

            @get:NotBlank
            @get:NotNull
            override val courseId: String?,

            @get:NotNull
            override val order: Int? = 0,
        ) : Request()

        @Schema(description = "Module update request")
        data class Update(
            @get:NotBlank
            @get:NotNull
            override val title: String?,

            @get:NotBlank
            @get:NotNull
            override val description: String?,

            override val parentId: String? = null,

            @get:NotBlank
            @get:NotNull
            override val courseId: String?,

            @get:NotNull
            override val order: Int? = 0,

            @get:NotBlank
            @get:NotNull
            override val id: String?
        ) : Request()

        @Schema(description = "Module deletion request")
        data class Delete(
            @get:NotBlank
            @get:NotNull
            override val id: String?
        ) : Request()

        data class GetAll(
            @get:NotNull
            val limit: Int = 10,
        )
    }

    sealed class Response : ModuleDto() {
        data class Base(
            override val id: String?,
            override val title: String?,
            override val description: String?,
            override val parentId: String?,
            override val courseId: String?,
            override val order: Int?
        ) : Response()
    }
}
