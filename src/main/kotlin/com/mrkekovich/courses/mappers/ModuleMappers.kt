package com.mrkekovich.courses.mappers

import com.mrkekovich.courses.dto.BaseModuleResponse
import com.mrkekovich.courses.dto.CreateModuleRequest
import com.mrkekovich.courses.dto.ModuleDto
import com.mrkekovich.courses.dto.UpdateModuleRequest
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.models.ModuleEntity
import com.mrkekovich.courses.repositories.CourseRepository
import com.mrkekovich.courses.repositories.ModuleRepository
import kotlin.jvm.optionals.getOrNull

fun ModuleEntity.toBaseResponseDto(): BaseModuleResponse {
    return BaseModuleResponse(
        id = id,
        title = title,
        description = description,
        parentModuleId = parentModule?.id,
        courseId = course?.id,
        position = position
    )
}

fun UpdateModuleRequest.toEntity(
    moduleRepository: ModuleRepository,
    courseRepository: CourseRepository,
): ModuleEntity = dtoToEntity(
    this,
    moduleRepository,
    courseRepository
)

fun CreateModuleRequest.toEntity(
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

    course = dto.courseId?.let {
        courseRepository.findById(it).getOrNull()
    } ?: throw NotFoundException("Course with id \"${dto.courseId}\" not found"),

    parentModule = dto.parentModuleId?.let {
        // We don't need to check if parent exists, if it's null
        moduleRepository.findById(it).getOrNull()
            ?: throw NotFoundException("Module with id \"${dto.parentModuleId}\" not found")
    },

    position = dto.position,

    id = dto.id
)
