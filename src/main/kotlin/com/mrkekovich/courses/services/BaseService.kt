package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseDto
import com.mrkekovich.courses.models.EntityInterface
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.jvm.optionals.getOrNull

/**
 * Base service for all services.
 * Forces child classes to implement CRUD methods.
 * @param T entity type
 * @param ID entity ID type
 * @param RS response type
 * @property repository JPA repository
 */
abstract class BaseService<T : EntityInterface<ID>, ID : Any, RS : BaseDto<T, ID>>(
    private val repository: JpaRepository<T, ID>,
) {
    abstract fun toResponse(entity: T): RS

    /**
     * Get all records from the database.
     * @return List of records wrapped in [RS] and [ResponseEntity].
     */
    open fun getAll(): ResponseEntity<List<RS>> {
        val response = repository.findAll().map {
            toResponse(it)
        }
        return ResponseEntity(
            response,
            HttpStatus.OK
        )
    }

    /**
     * Get record by ID from the database.
     * @param id ID of the record to get.
     * @return Record wrapped in [RS] and [ResponseEntity].
     */
    open fun getById(
        id: ID
    ): ResponseEntity<RS> {
        val entity = repository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity(
            toResponse(entity),
            HttpStatus.OK
        )
    }

    /**
     * Create new record in the database.
     * @param RQ Request type.
     * @param dto Request data.
     * @return New record wrapped in [RS] and [ResponseEntity].
     */
    open fun <RQ : BaseDto<T, ID>> create(
        dto: RQ
    ): ResponseEntity<RS> {
        val entity = repository.save(dto.toEntity())
        return ResponseEntity(
            toResponse(entity),
            HttpStatus.CREATED
        )
    }

    /**
     * Update existing record in the database.
     * @param RQ Request type.
     * @param id ID of the record to update.
     * @param dto Request data.
     * @return Updated record wrapped in [RS] and [ResponseEntity].
     */
    open fun <RQ : BaseDto<T, ID>> update(
        id: ID,
        dto: RQ
    ): ResponseEntity<RS> {
        repository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )
        val newEntity = repository.save(dto.toEntity(id))
        return ResponseEntity(
            toResponse(newEntity),
            HttpStatus.OK
        )
    }

    /**
     * Delete existing record in the database.
     * @param id ID of the record to delete.
     * @return [HttpStatus] wrapped in [ResponseEntity].
     */
    open fun delete(id: ID): ResponseEntity<HttpStatus> {
        val entity = repository.findById(id).getOrNull()
            ?: return ResponseEntity(
                HttpStatus.NOT_FOUND
            )

        repository.delete(entity)
        return ResponseEntity(
            HttpStatus.OK
        )
    }
}