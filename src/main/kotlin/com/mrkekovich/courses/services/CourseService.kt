package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import jakarta.ws.rs.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

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
        val entity = courseRepository.findById(id).get()
        val response = CourseDto.Response(entity)
        return ResponseEntity<CourseDto.Response>(
            response,
            HttpStatus.OK
        )
    }

    fun create(course: CourseDto.Request): ResponseEntity<CourseDto.Response> {
        val entity = course.toEntity()
        courseRepository.save(entity)
        val response = CourseDto.Response(entity)
        return ResponseEntity<CourseDto.Response>(
            response,
            HttpStatus.CREATED
        )
    }

    fun update(
        id: String,
        course: CourseDto.Request,
    ): ResponseEntity<CourseEntity> {
        courseRepository.findById(id).orElseThrow {
            NotFoundException("Course with id $id not found")
        }
        courseRepository.save(
            course.toEntity(id = id)
        )

        return ResponseEntity<CourseEntity>(
            course.toEntity(id = id),
            HttpStatus.OK
        )
    }

    fun delete(id: String): ResponseEntity.BodyBuilder {
        courseRepository.deleteById(id)
        return ResponseEntity.ok()
    }

}
