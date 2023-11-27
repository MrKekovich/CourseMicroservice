package com.mrkekovich.courses.mappers

import com.mrkekovich.courses.dto.BaseCourseResponse
import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.dto.CreateCourseRequest
import com.mrkekovich.courses.dto.UpdateCourseRequest
import com.mrkekovich.courses.models.CourseEntity

/**
 * Converts [CourseEntity] to [BaseCourseResponse].
 *
 * @return [BaseCourseResponse] mapped from [CourseEntity].
 */
fun CourseEntity.toBaseResponseDto(): BaseCourseResponse = BaseCourseResponse(
    title = title,
    description = description,
    id = id
)

/**
 * Converts [CreateCourseRequest] request DTO to entity.
 *
 * @return [CourseEntity] mapped from [CreateCourseRequest].
 */
fun CreateCourseRequest.toEntity(): CourseEntity = dtoToEntity(
    this
)

/**
 * Converts [UpdateCourseRequest] to entity
 *
 * @return [CourseEntity] mapped from [UpdateCourseRequest].
 */
fun UpdateCourseRequest.toEntity(): CourseEntity {
    return dtoToEntity(this)
}

/**
 * Converts [CourseDto] to [CourseEntity].
 *
 * [CourseDto] has all fields we need to set to null.
 * So be careful when creating an entity from DTO that does not
 * override all fields - they will be set to null.
 *
 * @param dto [CourseDto] to convert.
 * @return [CourseEntity] mapped from [dto].
 */
private fun dtoToEntity(
    dto: CourseDto
): CourseEntity = CourseEntity(
    title = dto.title,
    description = dto.description,
    id = dto.id,
)
