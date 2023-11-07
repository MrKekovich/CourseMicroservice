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

    fun getAll(): ResponseEntity<List<CourseDto.Response>> {
        val response = courseRepository.findAll().map {
            CourseDto.Response(it)
        }
        return ResponseEntity<List<CourseDto.Response>>(
            response,
            HttpStatus.OK
        )
    }

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

    fun create(course: CourseDto.Request): ResponseEntity<CourseDto.Response> {
        val entity = courseRepository.save(course.toEntity())

        return ResponseEntity<CourseDto.Response>(
            CourseDto.Response(entity),
            HttpStatus.CREATED
        )
    }

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

    fun delete(id: String): ResponseEntity<Unit> {
        courseRepository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )

        courseRepository.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }

}
