package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.ModuleDto
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.mappers.toBaseResponse
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
    fun create(dto: ModuleDto.Request.Create): ResponseEntity<ModuleDto.Response.Base> {
        val entity = moduleRepository.save(
            dto.toEntity(
                moduleRepository = moduleRepository,
                courseRepository = courseRepository,
            )
        )

        return ResponseEntity(
            entity.toBaseResponse(),
            HttpStatus.OK
        )
    }

    @Suppress("UnusedParameter")
    fun getAll(dto: ModuleDto.Request.GetAll): ResponseEntity<List<ModuleDto.Response.Base>> {
        val response = moduleRepository.findAll().map {
            it.toBaseResponse()
        }

        return ResponseEntity(
            response,
            HttpStatus.OK
        )
    }

    fun update(dto: ModuleDto.Request.Update): ResponseEntity<ModuleDto.Response.Base> {
        dto.id?.let {
            moduleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Module with id ${dto.id} not found")

        val updatedEntity = dto.toEntity(
            moduleRepository = moduleRepository,
            courseRepository = courseRepository,
        )

        return ResponseEntity(
            updatedEntity.toBaseResponse(),
            HttpStatus.OK
        )
    }

    fun delete(dto: ModuleDto.Request.Delete): ResponseEntity<HttpStatus> {
        val entity = dto.id?.let {
            moduleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Module with id ${dto.id} not found")

        moduleRepository.delete(entity)

        return ResponseEntity(HttpStatus.OK)
    }
}
