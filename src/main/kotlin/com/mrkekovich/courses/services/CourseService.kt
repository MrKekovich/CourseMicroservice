package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseCourseResponse
import com.mrkekovich.courses.dto.CreateCourseRequest
import com.mrkekovich.courses.dto.DeleteCourseRequest
import com.mrkekovich.courses.dto.UpdateCourseRequest
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.mappers.toBaseResponseDto
import com.mrkekovich.courses.mappers.toEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

private const val COURSE_NOT_FOUND_MESSAGE = "Course with id \"%s\" not found"

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    fun getAll(): List<BaseCourseResponse> =
        courseRepository.findAll().map {
            it.toBaseResponseDto()
        }

    fun create(
        dto: CreateCourseRequest,
    ): BaseCourseResponse {
        val entity = courseRepository.save(
            dto.toEntity()
        )

        return entity.toBaseResponseDto()
    }

    fun update(
        dto: UpdateCourseRequest,
    ): BaseCourseResponse {
        dto.id?.let { id ->
            courseRepository.findById(id).getOrNull()
        } ?: throw NotFoundException(COURSE_NOT_FOUND_MESSAGE.format(dto.id))

        val updatedEntity = courseRepository.save(dto.toEntity())

        return updatedEntity.toBaseResponseDto()
    }

    fun delete(
        dto: DeleteCourseRequest,
    ) {
        val entity = dto.id?.let {
            courseRepository.findById(it).getOrNull()
        } ?: throw NotFoundException(COURSE_NOT_FOUND_MESSAGE.format(dto.id))

        courseRepository.delete(entity)
    }
}
