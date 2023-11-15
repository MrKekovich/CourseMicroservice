package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.stereotype.Service

/**
 * Course service implementation.
 * @property courseRepository Course repository.
 */
@Service
class CourseService(
    private val courseRepository: CourseRepository
) : AbstractCrudService<CourseEntity, String, CourseDto.Response>(
    repository = courseRepository,
) {
    override fun toResponse(entity: CourseEntity): CourseDto.Response =
        CourseDto.Response(entity)
}