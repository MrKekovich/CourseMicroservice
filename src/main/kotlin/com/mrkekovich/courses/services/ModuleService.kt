package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseModuleResponse
import com.mrkekovich.courses.dto.CreateModuleRequest
import com.mrkekovich.courses.dto.DeleteModuleRequest
import com.mrkekovich.courses.dto.UpdateModuleRequest
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.mappers.toBaseResponseDto
import com.mrkekovich.courses.mappers.toEntity
import com.mrkekovich.courses.repositories.CourseRepository
import com.mrkekovich.courses.repositories.ModuleRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

private const val MODULE_NOT_FOUND_MESSAGE = "Module with id \"%s\" not found"

@Service
class ModuleService(
    private val moduleRepository: ModuleRepository,
    private val courseRepository: CourseRepository,
) {
    fun create(
        dto: CreateModuleRequest
    ): BaseModuleResponse {
        val entity = moduleRepository.save(
            dto.toEntity(
                moduleRepository = moduleRepository,
                courseRepository = courseRepository,
            )
        )

        return entity.toBaseResponseDto()
    }

    @Suppress("UnusedParameter")
    fun getAll(): List<BaseModuleResponse> =
        moduleRepository.findAll().map {
            it.toBaseResponseDto()
        }

    fun update(
        dto: UpdateModuleRequest
    ): BaseModuleResponse {
        dto.id?.let {
            moduleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException(MODULE_NOT_FOUND_MESSAGE.format(dto.id))

        val updatedEntity = moduleRepository.save(
            dto.toEntity(
                moduleRepository = moduleRepository,
                courseRepository = courseRepository,
            )
        )

        return updatedEntity.toBaseResponseDto()
    }

    fun delete(
        dto: DeleteModuleRequest
    ) {
        val entity = dto.id?.let {
            moduleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException(MODULE_NOT_FOUND_MESSAGE.format(dto.id))

        moduleRepository.delete(entity)
    }
}
