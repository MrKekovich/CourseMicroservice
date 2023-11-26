package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.BaseModuleResponse
import com.mrkekovich.courses.dto.CreateModuleRequest
import com.mrkekovich.courses.dto.DeleteModuleRequest
import com.mrkekovich.courses.dto.UpdateModuleRequest
import com.mrkekovich.courses.services.ModuleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    @Operation(summary = "Get all modules")
    @ApiResponse(
        responseCode = "200",
        description = "Get all modules",
        content = [
            Content(
                schema = Schema(implementation = BaseModuleResponse::class)
            )
        ]
    )
    @GetMapping
    fun getAll(
//        @Validated
//        @RequestBody
//        dto: GetAllModulesRequest TODO: add pagination
    ): ResponseEntity<List<BaseModuleResponse>> =
        moduleService.getAll(/*dto*/)

    @Operation(summary = "Create module")
    @ApiResponse(
        responseCode = "200",
        description = "Create module",
        content = [
            Content(
                schema = Schema(implementation = BaseModuleResponse::class)
            )
        ]
    )
    @PostMapping
    fun create(
        @Validated
        @RequestBody
        dto: CreateModuleRequest
    ): ResponseEntity<BaseModuleResponse> =
        moduleService.create(dto)

    @Operation(summary = "Update module")
    @ApiResponse(
        responseCode = "200",
        description = "Update module",
        content = [
            Content(
                schema = Schema(implementation = BaseModuleResponse::class)
            )
        ]
    )
    @PatchMapping
    fun update(
        @Validated
        @RequestBody
        dto: UpdateModuleRequest
    ): ResponseEntity<BaseModuleResponse> =
        moduleService.update(dto)

    @Operation(summary = "Delete module")
    @ApiResponse(
        responseCode = "200",
        description = "Delete module",
    )
    @DeleteMapping
    fun delete(
        @Validated
        @RequestBody
        dto: DeleteModuleRequest
    ): ResponseEntity<HttpStatus> =
        moduleService.delete(dto)
}
