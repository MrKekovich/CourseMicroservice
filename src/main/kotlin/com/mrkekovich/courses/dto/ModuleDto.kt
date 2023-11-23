package com.mrkekovich.courses.dto

import com.fasterxml.jackson.annotation.JsonProperty
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
    @get:JsonProperty("parent_id")
    open val parentId: String? = null

    @get:Schema(description = "Parent course id")
    @get:JsonProperty("course_id")
    open val courseId: String? = null

    @get:Schema(description = "Module position")
    open val position: Int? = null

    @get:Schema(description = "Module id")
    open val id: String? = null

    sealed class Request : ModuleDto() {
        @Schema(name = "Create module request")
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
            override val position: Int? = 0,
        ) : Request()

        @Schema(name = "Update module request")
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
            override val position: Int? = 0,

            @get:NotBlank
            @get:NotNull
            override val id: String?
        ) : Request()

        @Schema(name = "Delete module request")
        data class Delete(
            @get:NotBlank
            @get:NotNull
            override val id: String?
        ) : Request()

        @Schema(name = "Get all modules request")
        data class GetAll(
            @get:NotNull
            val limit: Int = 10,
        )
    }

    sealed class Response : ModuleDto() {
        @Schema(name = "Base module response")
        data class Base(
            override val id: String?,
            override val title: String?,
            override val description: String?,
            override val parentId: String?,
            override val courseId: String?,
            override val position: Int?
        ) : Response()
    }
}
