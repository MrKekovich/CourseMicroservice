package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.services.CourseService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService
) {
    /**
     * Gets all courses.
     * @return Response entity containing list of all courses.
     */
    @GetMapping
    fun getAll(): ResponseEntity<List<CourseDto.Response>> {
        return courseService.getAll()
    }

    /**
     * Gets course by its id.
     * @param id the id of course to get.
     * @return Response entity containing specified course.
     */
    @GetMapping("/{id}")
    fun getById(
        @Validated
        @PathVariable
        id: String
    ): ResponseEntity<CourseDto.Response> {
        return courseService.getById(id)
    }

    /**
     * Creates new course.
     * @param course json request body with course data.
     * @return Response entity containing created course.
     */
    @PostMapping("/create")
    fun create(
        @Validated
        @RequestBody
        course: CourseDto.Request
    ): ResponseEntity<CourseDto.Response> {
        return courseService.create(course)
    }

    /**
     * Updates specified course.
     * @param id the id of course to update.
     * @param course json request body with course data.
     * @return Response entity containing updated course.
     */
    @PatchMapping("/update/{id}")
    fun update(
        @Validated
        @PathVariable
        id: String,

        @Validated
        @RequestBody
        course: CourseDto.Request
    ): ResponseEntity<CourseDto.Response> {
        return courseService.update(id, course)
    }

    /**
     * Deletes specified course.
     * @param id the id of course to delete.
     * @return Response entity containing http status.
     */
    @DeleteMapping("/delete/{id}")
    fun delete(
        @Validated
        @PathVariable
        id: String
    ): ResponseEntity<Unit> {
        return courseService.delete(id)
    }
}