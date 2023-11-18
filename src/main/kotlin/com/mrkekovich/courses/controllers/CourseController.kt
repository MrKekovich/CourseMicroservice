package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.services.CourseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/courses")
@RestController
@Tag(name = "Course controller")
class CourseController(
    private val courseService: CourseService,
) {
    @Operation(summary = "Get all courses")
    @ApiResponse(
        responseCode = "200",
        description = "Get all course with a limit",
        content = [
            Content(
                schema = Schema(implementation = CourseDto.Response.Base::class)
            )
        ]
    )
    @GetMapping("/get/all")
    fun getCourses(
        @Validated
        dto: CourseDto.Request.GetAll,
    ): ResponseEntity<List<CourseDto.Response.Base>> =
        courseService.getCourses(dto)

    @Operation(summary = "Create course")
    @ApiResponse(
        responseCode = "200",
        description = "Create course",
        content = [
            Content(
                schema = Schema(implementation = CourseDto.Response.Base::class)
            )
        ]
    )
    @PostMapping("/create")
    fun createCourse(
        @Validated
        dto: CourseDto.Request.Create,
    ): ResponseEntity<CourseDto.Response.Base> =
        courseService.createCourse(dto)

    @Operation(summary = "Update course")
    @ApiResponse(
        responseCode = "200",
        description = "Update course",
        content = [
            Content(
                schema = Schema(implementation = CourseDto.Response.Base::class)
            )
        ]
    )
    @PatchMapping("/update")
    fun updateCourse(
        @Validated
        dto: CourseDto.Request.Update,
    ): ResponseEntity<CourseDto.Response.Base> =
        courseService.updateCourse(dto)

    @Operation(summary = "Delete course")
    @ApiResponse(
        responseCode = "200",
        description = "Delete course",
    )
    @DeleteMapping("/delete")
    fun deleteCourse(
        @Validated
        dto: CourseDto.Request.Delete,
    ): ResponseEntity<HttpStatus> =
        courseService.deleteCourse(dto)
}
