package com.mrkekovich.coursemicroservice.controllers

import com.mrkekovich.coursemicroservice.dto.BaseModuleResponse
import com.mrkekovich.coursemicroservice.dto.CreateModuleRequest
import com.mrkekovich.coursemicroservice.dto.DeleteModuleRequest
import com.mrkekovich.coursemicroservice.dto.UpdateModuleRequest
import com.mrkekovich.coursemicroservice.services.ModuleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/modules")
@Tag(name = "Module controller")
class ModuleController(
    private val moduleService: ModuleService
) {
    @ApiResponse(
        responseCode = "200",
        description = "Get modules",
        content = [
            Content(
                schema = Schema(implementation = BaseModuleResponse::class)
            )
        ]
    )
    @Operation(summary = "Get modules")
    @GetMapping
    fun getModules(): List<BaseModuleResponse> =
        moduleService.getAll()

    @ApiResponse(
        responseCode = "200",
        description = "Create module",
        content = [
            Content(
                schema = Schema(implementation = BaseModuleResponse::class)
            )
        ]
    )
    @Operation(summary = "Create module")
    @PostMapping
    fun createModule(
        @Validated
        @RequestBody
        dto: CreateModuleRequest
    ): BaseModuleResponse =
        moduleService.create(dto)

    @ApiResponse(
        responseCode = "200",
        description = "Update module",
        content = [
            Content(
                schema = Schema(implementation = BaseModuleResponse::class)
            )
        ]
    )
    @Operation(summary = "Update module")
    @PatchMapping
    fun updateModule(
        @Validated
        @RequestBody
        dto: UpdateModuleRequest
    ): BaseModuleResponse =
        moduleService.update(dto)

    @ApiResponse(
        responseCode = "200",
        description = "Delete module",
    )
    @DeleteMapping
    @Operation(summary = "Delete module")
    fun deleteModule(
        @Validated
        @RequestBody
        dto: DeleteModuleRequest
    ): Unit =
        moduleService.delete(dto)
}
