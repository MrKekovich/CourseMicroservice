package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    private fun CourseEntity.toResponse(): CourseDto.Response.Base =
        CourseDto.Response.Base(this)

    private fun CourseDto.Request.Create.toEntity(): CourseEntity =
        CourseEntity(
            title = this.title,
            description = this.description
        )

    fun getCourses(
        dto: CourseDto.Request.GetAll,
    ): ResponseEntity<List<CourseDto.Response.Base>> {
        val response = courseRepository.findAll().map {
            it.toResponse()
        }

        return ResponseEntity.ok(response)
    }

    fun createCourse(
        dto: CourseDto.Request.Create,
    ): ResponseEntity<CourseDto.Response.Base> {
        val entity = courseRepository.save(
            dto.toEntity()
        )
        return ResponseEntity.ok(entity.toResponse())
    }

    fun updateCourse(
        dto: CourseDto.Request.Update,
    ): ResponseEntity<CourseDto.Response.Base> {
        val entity = courseRepository.findById(dto.id).getOrElse {
            throw NotFoundException("Course ${dto.id} not found")
        }
        val newEntity = courseRepository.save(
            entity.copy(
                title = dto.title,
                description = dto.description
            )
        )

        return ResponseEntity.ok(newEntity.toResponse())
    }

    fun deleteCourse(
        dto: CourseDto.Request.Delete,
    ): ResponseEntity<HttpStatus> {
        val entity = courseRepository.findById(dto.id).getOrElse {
            throw NotFoundException("Course ${dto.id} not found")
        }
        courseRepository.delete(entity)
        return ResponseEntity(HttpStatus.OK)
    }
}
