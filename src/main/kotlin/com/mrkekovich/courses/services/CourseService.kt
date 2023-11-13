package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseDto
import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

/**
 * Course service implementation.
 * @property courseRepository Course repository.
 */
@Service
class CourseService(
    private val courseRepository: CourseRepository
): BaseService<CourseEntity, String, CourseDto.Response>(courseRepository) {
    override fun getAll(): ResponseEntity<List<CourseDto.Response>> {
        val response = courseRepository.findAll().map {
            CourseDto.Response(it)
        }
        return ResponseEntity(
            response,
            HttpStatus.OK
        )
    }

    override fun <RQ : BaseDto<CourseEntity, String>> create(
        dto: RQ
    ): ResponseEntity<CourseDto.Response> {
        val course = courseRepository.save(dto.toEntity())
        return ResponseEntity(
            CourseDto.Response(course),
            HttpStatus.CREATED
        )
    }

    override fun <RQ : BaseDto<CourseEntity, String>> update(
        id: String,
        dto: RQ
    ): ResponseEntity<CourseDto.Response> {
        courseRepository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )

        val newCourse = courseRepository.save(dto.toEntity(id = id))
        return ResponseEntity(
            CourseDto.Response(newCourse),
            HttpStatus.OK
        )
    }

    override fun getById(id: String): ResponseEntity<CourseDto.Response> {
        val course = courseRepository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity(
            CourseDto.Response(course),
            HttpStatus.OK
        )
    }
}