package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.ModuleDto
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.models.ModuleEntity
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
    private fun ModuleDto.Request.Update.toEntity(): ModuleEntity {
        return ModuleEntity(
            title = title,
            description = description,
            parentModule = parentId?.let {
                moduleRepository.findById(it).getOrNull()
            },
            course = courseId?.let {
                courseRepository.findById(it).getOrNull()
            } ?: throw NotFoundException("Course with id $courseId not found"),
            order = order,
            id = id
        )
    }

    private fun ModuleDto.Request.Create.toEntity(): ModuleEntity {
        return ModuleEntity(
            title = title,
            description = description,
            parentModule = parentId?.let {
                moduleRepository.findById(it).getOrNull()
            },
            course = courseId?.let {
                courseRepository.findById(it).getOrNull()
            } ?: throw NotFoundException("Course with id $courseId not found"),
            order = order
        )
    }

    fun create(dto: ModuleDto.Request.Create): ResponseEntity<ModuleDto.Response.Base> {
        val entity = moduleRepository.save(dto.toEntity())
        return ResponseEntity(
            entity.toBaseResponse(),
            HttpStatus.OK
        )
    }

    @Suppress("UnusedParameter")
    fun getAll(dto: ModuleDto.Request.GetAll): List<ModuleDto.Response.Base> {
        return moduleRepository.findAll().map { it.toBaseResponse() }
    }

    fun update(dto: ModuleDto.Request.Update): ResponseEntity<ModuleDto.Response.Base> {
        dto.id?.let {
            moduleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Module with id ${dto.id} not found")

        val updatedEntity = dto.toEntity()
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

private fun ModuleEntity.toBaseResponse(): ModuleDto.Response.Base {
    return ModuleDto.Response.Base(
        id = id,
        title = title,
        description = description,
        parentId = parentModule?.id,
        courseId = course?.id,
        order = order
    )
}
