package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CourseService(
    private val courseRepository: CourseRepository
) {
    /**
     * Gets all courses.
     * @return Response entity containing list of all courses.
     */
    fun getAll(): ResponseEntity<List<CourseDto.Response>> {
        val response = courseRepository.findAll().map {
            CourseDto.Response(it)
        }
        return ResponseEntity<List<CourseDto.Response>>(
            response,
            HttpStatus.OK
        )
    }

    /**
     * Gets course by its id.
     * @param id the id of course to get.
     * @return Response entity containing specified course.
     */
    fun getById(id: String): ResponseEntity<CourseDto.Response> {
        val entity = courseRepository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )

        return ResponseEntity<CourseDto.Response>(
            CourseDto.Response(entity),
            HttpStatus.OK
        )
    }

    /**
     * Creates new course and returns it.
     * @param course json request body, validated by DTO, with course data
     * @return Response entity containing created course
     */
    fun create(course: CourseDto.Request): ResponseEntity<CourseDto.Response> {
        val entity = courseRepository.save(course.toEntity())

        return ResponseEntity<CourseDto.Response>(
            CourseDto.Response(entity),
            HttpStatus.CREATED
        )
    }

    /**
     * Updates course and returns it.
     * @param id the id of course to update.
     * @param course json request body, validated by DTO, with course data.
     * @return Response entity containing updated course.
     */
    fun update(
        id: String,
        course: CourseDto.Request,
    ): ResponseEntity<CourseDto.Response> {
        courseRepository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )

        val newEntity = courseRepository.save(
            course.toEntity(id = id)
        )

        return ResponseEntity<CourseDto.Response>(
            CourseDto.Response(newEntity),
            HttpStatus.OK
        )
    }

    /**
     * Deletes course by its id.
     * @param id the id of course to delete.
     * @return Response entity containing http status.
     */
    fun delete(id: String): ResponseEntity<Unit> {
        courseRepository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )

        courseRepository.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }

}
