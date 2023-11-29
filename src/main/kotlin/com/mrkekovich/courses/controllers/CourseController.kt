package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.BaseCourseResponse
import com.mrkekovich.courses.dto.CreateCourseRequest
import com.mrkekovich.courses.dto.DeleteCourseRequest
import com.mrkekovich.courses.dto.GetCoursesRequest
import com.mrkekovich.courses.dto.UpdateCourseRequest
import com.mrkekovich.courses.services.CourseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/courses")
@RestController
@Tag(name = "Course controller")
class CourseController(
    private val courseService: CourseService,
) {
    @ApiResponse(
        responseCode = "200",
        description = "Get course with a pageSize",
        content = [Content(schema = Schema(implementation = BaseCourseResponse::class))]
    )
    @Operation(summary = "Get courses")
    @GetMapping
    fun getCourses(
        @Validated
        @RequestBody
        dto: GetCoursesRequest
    ): Page<BaseCourseResponse> =
        courseService.getAll(dto)

    @ApiResponse(
        responseCode = "200",
        description = "Create course",
        content = [Content(schema = Schema(implementation = BaseCourseResponse::class))]
    )
    @Operation(summary = "Create course")
    @PostMapping
    fun createCourse(
        @Validated
        @RequestBody
        dto: CreateCourseRequest,
    ): BaseCourseResponse =
        courseService.create(dto)

    @ApiResponse(
        responseCode = "200",
        description = "Update course",
        content = [Content(schema = Schema(implementation = BaseCourseResponse::class))]
    )
    @Operation(summary = "Update course")
    @PatchMapping
    fun updateCourse(
        @Validated
        @RequestBody
        dto: UpdateCourseRequest,
    ): BaseCourseResponse =
        courseService.update(dto)

    @ApiResponse(
        responseCode = "200",
        description = "Delete course",
    )
    @Operation(summary = "Delete course")
    @DeleteMapping
    fun deleteCourse(
        @Validated
        @RequestBody
        dto: DeleteCourseRequest,
    ): Unit =
        courseService.delete(dto)
}
