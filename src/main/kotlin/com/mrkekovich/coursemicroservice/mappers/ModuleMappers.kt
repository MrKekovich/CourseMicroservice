package com.mrkekovich.coursemicroservice.mappers

import com.mrkekovich.coursemicroservice.dto.BaseModuleResponse
import com.mrkekovich.coursemicroservice.dto.CreateModuleRequest
import com.mrkekovich.coursemicroservice.dto.ModuleDto
import com.mrkekovich.coursemicroservice.dto.UpdateModuleRequest
import com.mrkekovich.coursemicroservice.exceptions.NotFoundException
import com.mrkekovich.coursemicroservice.models.ModuleEntity
import com.mrkekovich.coursemicroservice.repositories.CourseRepository
import com.mrkekovich.coursemicroservice.repositories.ModuleRepository
import kotlin.jvm.optionals.getOrNull

/**
 * Converts [ModuleEntity] to [BaseModuleResponse]
 *
 * @return [BaseModuleResponse] mapped from [ModuleEntity].
 */
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

/**
 * Converts [CreateModuleRequest] to [ModuleEntity].
 *
 * @param moduleRepository [ModuleRepository] Used to map module from id.
 * @param courseRepository [CourseRepository] Used to map course from id.
 * @return [ModuleEntity] mapped from [CreateModuleRequest].
 */
fun CreateModuleRequest.toEntity(
    moduleRepository: ModuleRepository,
    courseRepository: CourseRepository,
): ModuleEntity = dtoToEntity(
    this,
    moduleRepository,
    courseRepository
)

/**
 * Converts [UpdateModuleRequest] to [ModuleEntity]
 *
 * @param moduleRepository [ModuleRepository]
 * @param courseRepository [CourseRepository]
 * @return [ModuleEntity] mapped from [UpdateModuleRequest]
 */
fun UpdateModuleRequest.toEntity(
    moduleRepository: ModuleRepository,
    courseRepository: CourseRepository,
): ModuleEntity = dtoToEntity(
    this,
    moduleRepository,
    courseRepository
)

/**
 * Dto to entity
 *
 * Converts dto to an entity.
 * By default, parent DTOs have all fields we need set to null.
 * So be careful when creating an entity from DTO that does not
 * override all fields - they will be set to null.
 *
 * @param dto The [ModuleDto] to convert to entity.
 * @param moduleRepository [ModuleRepository] Used to map module from id.
 * @param courseRepository [CourseRepository] Used to map course from id.
 * @return [ModuleEntity] mapped from [dto].
 */
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
