package com.mrkekovich.courses.mappers

import com.mrkekovich.courses.dto.ModuleDto
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.models.ModuleEntity
import com.mrkekovich.courses.repositories.CourseRepository
import com.mrkekovich.courses.repositories.ModuleRepository
import kotlin.jvm.optionals.getOrNull

fun ModuleEntity.toBaseResponse(): ModuleDto.Response.Base {
    return ModuleDto.Response.Base(
        id = id,
        title = title,
        description = description,
        parentId = parentModule?.id,
        courseId = course?.id,
        position = position
    )
}

fun ModuleDto.Request.Update.toEntity(
    moduleRepository: ModuleRepository,
    courseRepository: CourseRepository,
): ModuleEntity = dtoToEntity(
    this,
    moduleRepository,
    courseRepository
)

fun ModuleDto.Request.Create.toEntity(
    moduleRepository: ModuleRepository,
    courseRepository: CourseRepository,
): ModuleEntity = dtoToEntity(
    this,
    moduleRepository,
    courseRepository
)

private fun dtoToEntity(
    dto: ModuleDto,
    moduleRepository: ModuleRepository,
    courseRepository: CourseRepository
): ModuleEntity = ModuleEntity(
    title = dto.title,
    description = dto.description,
    parentModule = dto.parentId?.let {
        moduleRepository.findById(it).getOrNull()
    },
    course = dto.courseId?.let {
        courseRepository.findById(it).getOrNull()
    } ?: throw NotFoundException("Course with id ${dto.courseId} not found"),
    position = dto.position,
    id = dto.id
)
