package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.services.CourseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService
) : BaseController<CourseEntity, String, CourseDto.Response, CourseDto.Request>() {
    @GetMapping
    override fun getAll(): ResponseEntity<List<CourseDto.Response>> =
        courseService.getAll()

    @GetMapping("/{id}")
    override fun getById(
        @Validated
        @PathVariable
        id: String
    ): ResponseEntity<CourseDto.Response> =
        courseService.getById(id)

    @PostMapping
    override fun create(
        @Validated
        @RequestBody
        dto: CourseDto.Request
    ): ResponseEntity<CourseDto.Response> =
        courseService.create(dto)

    @PatchMapping("/{id}")
    override fun update(
        @Validated
        @PathVariable
        id: String,

        @Validated
        @RequestBody
        dto: CourseDto.Request
    ): ResponseEntity<CourseDto.Response> =
        courseService.update(id, dto)

    @DeleteMapping("/{id}")
    override fun delete(
        @PathVariable
        id: String
    ): ResponseEntity<HttpStatus> =
        courseService.delete(id)
}