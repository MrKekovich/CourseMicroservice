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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ModuleService(
    private val moduleRepository: ModuleRepository,
    private val courseRepository: CourseRepository,
) {
    fun create(dto: CreateModuleRequest): ResponseEntity<BaseModuleResponse> {
        val entity = moduleRepository.save(
            dto.toEntity(
                moduleRepository = moduleRepository,
                courseRepository = courseRepository,
            )
        )

        return ResponseEntity(
            entity.toBaseResponseDto(),
            HttpStatus.CREATED
        )
    }

    @Suppress("UnusedParameter")
    fun getAll(
//        dto: GetAllModulesRequest TODO: add pagination
    ): ResponseEntity<List<BaseModuleResponse>> {
        val response = moduleRepository.findAll().map {
            it.toBaseResponseDto()
        }

        return ResponseEntity(
            response,
            HttpStatus.OK
        )
    }

    fun update(dto: UpdateModuleRequest): ResponseEntity<BaseModuleResponse> {
        dto.id?.let {
            moduleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Module with id \"${dto.id}\" not found")

        val updatedEntity = moduleRepository.save(
            dto.toEntity(
                moduleRepository = moduleRepository,
                courseRepository = courseRepository,
            )
        )

        return ResponseEntity(
            updatedEntity.toBaseResponseDto(),
            HttpStatus.OK
        )
    }

    fun delete(dto: DeleteModuleRequest): ResponseEntity<HttpStatus> {
        val entity = dto.id?.let {
            moduleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Module with id \"${dto.id}\" not found")

        moduleRepository.delete(entity)

        return ResponseEntity(HttpStatus.OK)
    }
}
