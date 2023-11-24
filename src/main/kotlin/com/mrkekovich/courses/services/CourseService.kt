package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseCourseResponse
import com.mrkekovich.courses.dto.CreateCourseRequest
import com.mrkekovich.courses.dto.DeleteCourseRequest
import com.mrkekovich.courses.dto.GetAllCoursesRequest
import com.mrkekovich.courses.dto.UpdateCourseRequest
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
        dto: GetAllCoursesRequest,
    ): ResponseEntity<List<BaseCourseResponse>> {
        val response = courseRepository.findAll().map {
            it.toBaseResponseDto()
        }

        return ResponseEntity.ok(response)
    }

    fun create(
        dto: CreateCourseRequest,
    ): ResponseEntity<BaseCourseResponse> {
        val entity = courseRepository.save(
            dto.toEntity()
        )
        return ResponseEntity.ok(entity.toBaseResponseDto())
    }

    fun update(
        dto: UpdateCourseRequest,
    ): ResponseEntity<BaseCourseResponse> {
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
        dto: DeleteCourseRequest,
    ): ResponseEntity<HttpStatus> {
        val entity = dto.id?.let {
            courseRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Course ${dto.id} not found")

        courseRepository.delete(entity)

        return ResponseEntity(HttpStatus.OK)
    }
}
