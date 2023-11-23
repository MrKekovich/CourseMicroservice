package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.mappers.toBaseResponseDto
import com.mrkekovich.courses.mappers.toEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    @Suppress("UnusedParameter")
    fun getAll(
        dto: CourseDto.Request.GetAll,
    ): ResponseEntity<List<CourseDto.Response.Base>> {
        val response = courseRepository.findAll().map {
            it.toBaseResponseDto()
        }

        return ResponseEntity.ok(response)
    }

    fun create(
        dto: CourseDto.Request.Create,
    ): ResponseEntity<CourseDto.Response.Base> {
        val entity = courseRepository.save(
            dto.toEntity()
        )
        return ResponseEntity.ok(entity.toBaseResponseDto())
    }

    fun update(
        dto: CourseDto.Request.Update,
    ): ResponseEntity<CourseDto.Response.Base> {
        dto.id?.let { id ->
            courseRepository.findById(id).getOrNull()
        } ?: throw NotFoundException("Course ${dto.id} not found")

        val updatedEntity = courseRepository.save(dto.toEntity())
        return ResponseEntity(
            updatedEntity.toBaseResponseDto(),
            HttpStatus.OK,
        )
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
