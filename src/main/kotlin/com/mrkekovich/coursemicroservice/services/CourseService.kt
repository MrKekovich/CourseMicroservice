package com.mrkekovich.coursemicroservice.services

import com.mrkekovich.coursemicroservice.dto.BaseCourseResponse
import com.mrkekovich.coursemicroservice.dto.CreateCourseRequest
import com.mrkekovich.coursemicroservice.dto.DeleteCourseRequest
import com.mrkekovich.coursemicroservice.dto.GetCoursesRequest
import com.mrkekovich.coursemicroservice.dto.UpdateCourseRequest
import com.mrkekovich.coursemicroservice.exceptions.NotFoundException
import com.mrkekovich.coursemicroservice.mappers.toBaseResponseDto
import com.mrkekovich.coursemicroservice.mappers.toEntity
import com.mrkekovich.coursemicroservice.repositories.CourseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

private const val COURSE_NOT_FOUND_MESSAGE = "Course with id \"%s\" not found"
private const val DEFAULT_PAGE = 0
private const val DEFAULT_PAGE_SIZE = 10

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    fun getAll(
        dto: GetCoursesRequest,
    ): Page<BaseCourseResponse> =
        courseRepository.findAllByFilter(
            title = dto.title,
            description = dto.description,
            id = dto.id,
            pageable = PageRequest.of(
                dto.page ?: DEFAULT_PAGE,
                dto.pageSize ?: DEFAULT_PAGE_SIZE,
            )
        ).map {
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
        val entity = dto.id?.let { id ->
            courseRepository.findById(id).getOrNull()
        } ?: throw NotFoundException(COURSE_NOT_FOUND_MESSAGE.format(dto.id))

        courseRepository.delete(entity)
    }
}
