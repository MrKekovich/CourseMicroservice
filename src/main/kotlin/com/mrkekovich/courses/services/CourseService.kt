package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseDto
import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CourseService(
    private val courseRepository: CourseRepository
): BaseService<CourseEntity, String>(courseRepository) {
    override fun getAll(): ResponseEntity<out List<BaseDto<CourseEntity, String>>> {
        val response = courseRepository.findAll().map {
            CourseDto.Response(it)
        }
        return ResponseEntity(
            response,
            HttpStatus.OK
        )
    }

    override fun <Request : BaseDto<CourseEntity, String>> create(dto: Request): ResponseEntity<out BaseDto<CourseEntity, String>> {
        val course = courseRepository.save(dto.toEntity())
        return ResponseEntity(
            CourseDto.Response(course),
            HttpStatus.CREATED
        )
    }

    override fun <Request : BaseDto<CourseEntity, String>> update(
        id: String,
        dto: Request
    ): ResponseEntity<out BaseDto<CourseEntity, String>> {
        val course = courseRepository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )
        val newCourse = courseRepository.save(dto.toEntity(id = id))
        return ResponseEntity(
            CourseDto.Response(newCourse),
            HttpStatus.OK
        )
    }

    override fun getById(id: String): ResponseEntity<out BaseDto<CourseEntity, String>> {
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