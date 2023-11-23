package com.mrkekovich.courses.mappers

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.models.CourseEntity

/**
 * Converts [CourseEntity] to [CourseDto.Response.Base].
 *
 * @return [CourseDto.Response.Base] mapped from [CourseEntity].
 */
fun CourseEntity.toBaseResponseDto(): CourseDto.Response.Base = CourseDto.Response.Base(
    title = title,
    description = description,
    id = id
)

/**
 * Converts [CourseDto.Request.Create] request DTO to entity.
 *
 * @return [CourseEntity] mapped from [CourseDto.Request.Create].
 */
fun CourseDto.Request.Create.toEntity(): CourseEntity = dtoToEntity(
    this
)

/**
 * Converts [CourseDto.Request.Update] to entity
 *
 * @return [CourseEntity] mapped from [CourseDto.Request.Update].
 */
fun CourseDto.Request.Update.toEntity(): CourseEntity {
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
 * @return [CourseEntity] with mapped from [CourseDto].
 */
private fun dtoToEntity(
    dto: CourseDto
): CourseEntity = CourseEntity(
    title = dto.title,
    description = dto.description,
    id = dto.id,
)
