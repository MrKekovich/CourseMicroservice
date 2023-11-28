package com.mrkekovich.courses.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * Module Data Transfer Object.
 * Used to transfer validated data between client and server.
 *
 * @property title Title of a module.
 * - [Length]: (max = 255)
 *
 * @property description Description of a module.
 * - [Length]: (max = 1000)
 *
 * @property position Position of the module.
 *
 * @property parentModuleId ID of the parent module.
 * - [JsonProperty]: "parent_id"
 *
 * @property courseId ID of the course the module belongs to.
 * - [JsonProperty]: "course_id"
 *
 * @property id ID of the module.
 */
sealed class ModuleDto {
    @get:Schema(description = "Title of the module")
    @get:Length(max = 255)
    open val title: String? = null

    @get:Schema(description = "Description of the module")
    @get:Length(max = 1000)
    open val description: String? = null

    @get:Schema(description = "Position of the module")
    open val position: Int? = null

    @get:Schema(description = "ID of the parent module")
    @get:JsonProperty("parent_module_id")
    open val parentModuleId: String? = null

    @get:Schema(description = "ID of the course the module belongs to")
    @get:JsonProperty("course_id")
    open val courseId: String? = null

    @get:Schema(description = "ID of the module")
    open val id: String? = null
}

/**
 * Create module DTO represents a client request to create a new module.
 * Contains values to create a new module.
 *
 * @property title [ModuleDto.title]
 *  - [NotBlank]
 *
 * @property description [ModuleDto.description]
 * - [NotBlank]
 *
 * @property parentModuleId [ModuleDto.parentModuleId]
 *
 * @property courseId [ModuleDto.courseId]
 * - [NotBlank]
 *
 * @property position [ModuleDto.position]
 * - [NotNull]
 *
 * @property id [ModuleDto.id] - generated by server.
 */
@Schema(name = "Create module request")
data class CreateModuleRequest(
    @get:NotBlank
    override val title: String?,

    @get:NotBlank
    override val description: String?,

    @get:NotNull
    override val position: Int? = 0,

    override val parentModuleId: String? = null,

    @get:NotBlank
    override val courseId: String?,
) : ModuleDto()

/**
 * Update module DTO represents a client request to update a module.
 * Contains new values for the module to update.
 *
 * @property title [ModuleDto.title]
 * - [NotBlank]
 *
 * @property description [ModuleDto.description]
 * - [NotBlank]
 *
 * @property position [ModuleDto.position]
 * - [NotNull]
 *
 * @property parentModuleId [ModuleDto.parentModuleId]
 *
 * @property courseId [ModuleDto.courseId]
 * - [NotBlank]
 *
 * @property id ID of the module to update.
 * - [NotBlank]
 */
@Schema(name = "Update module request")
data class UpdateModuleRequest(
    @get:NotBlank
    override val title: String?,

    @get:NotBlank
    override val description: String?,

    @get:NotNull
    override val position: Int? = 0,

    override val parentModuleId: String? = null,

    @get:NotBlank
    override val courseId: String?,

    @get:NotBlank
    override val id: String?
) : ModuleDto()

/**
 * Delete module DTO represents a client request to delete a module.
 *
 * @property id ID of the module to delete.
 * - [NotBlank]
 */
@Schema(name = "Delete module request")
data class DeleteModuleRequest(
    @get:NotBlank
    override val id: String?
) : ModuleDto()

@Schema(name = "Get all modules request")
data class GetAllModulesRequest(
    @get:NotNull
    val limit: Int = 10,
) : ModuleDto()

/**
 * Base module DTO represents a module response returned from the server.
 *
 * @property title [ModuleDto.title]
 * @property description [ModuleDto.description]
 * @property position [ModuleDto.position]
 * @property parentModuleId [ModuleDto.parentModuleId]
 * @property courseId [ModuleDto.courseId]
 * @property id [ModuleDto.id]
 */
@Schema(name = "Base module response")
data class BaseModuleResponse(
    override val title: String?,
    override val description: String?,
    override val position: Int?,
    override val parentModuleId: String?,
    override val courseId: String?,
    override val id: String?,
) : ModuleDto()
