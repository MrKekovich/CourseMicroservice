package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.services.CourseService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService
) {
    @GetMapping
    fun getAll(): List<CourseDto.Response> {
        return courseService.getAll()
    }

    @GetMapping("/{id}")
    fun getById(
        @Validated
        @PathVariable
        id: String
    ): CourseDto.Response {
        return courseService.getById(id)
    }

    @PostMapping("/create")
    fun create(
        @Validated
        @RequestBody
        course: CourseDto.Request
    ): CourseDto.Response {
        return courseService.create(course)
    }

    @PatchMapping("/update/{id}")
    fun update(
        @Validated
        @PathVariable
        id: String,

        @Validated
        @RequestBody
        course: CourseDto.Request
    ): CourseDto.Response {
        return courseService.update(id, course)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(
        @Validated
        @PathVariable
        id: String
    ) {
        courseService.delete(id)
    }
}