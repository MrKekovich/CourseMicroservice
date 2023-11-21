package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    fun getAll(
        dto: CourseDto.Request.GetAll,
    ): ResponseEntity<List<CourseDto.Response.Base>> {
        val response = courseRepository.findAll().map {
            it.toResponse()
        }

        return ResponseEntity.ok(response)
    }

    fun create(
        dto: CourseDto.Request.Create,
    ): ResponseEntity<CourseDto.Response.Base> {
        val entity = courseRepository.save(
            dto.toEntity()
        )
        return ResponseEntity.ok(entity.toResponse())
    }

    fun update(
        dto: CourseDto.Request.Update,
    ): ResponseEntity<CourseDto.Response.Base> {
        val entity = dto.id?.let { id ->
            courseRepository.findById(id).getOrNull()
        } ?: throw NotFoundException("Course ${dto.id} not found")

        val newEntity = CourseEntity(
            title = dto.title,
            description = dto.description,
            id = entity.id
        ).apply {
            courseRepository.save(this)
        }

        return ResponseEntity.ok(newEntity.toResponse())
    }

    fun delete(
        dto: CourseDto.Request.Delete,
    ): ResponseEntity<HttpStatus> {
        val entity = dto.id?.let {
            courseRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Course ${dto.id} not found")

        courseRepository.delete(entity)

        return ResponseEntity(HttpStatus.OK)
    }
}

private fun CourseEntity.toResponse(): CourseDto.Response.Base =
    CourseDto.Response.Base(this)

private fun CourseDto.Request.Create.toEntity(): CourseEntity =
    CourseEntity(
        title = title,
        description = description
    )