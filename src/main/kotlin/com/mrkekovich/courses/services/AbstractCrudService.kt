package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.EntityDto
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.models.EntityInterface
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.jvm.optionals.getOrElse

/**
 * Provides basic CRUD operations.
 * @param T entity type
 * @param ID entity ID type
 * @param RS response type
 * @property repository JPA repository
 */
abstract class AbstractCrudService<
        T : EntityInterface<ID>,
        ID : Any,
        RS : EntityDto<T, ID>>(
    private val repository: JpaRepository<T, ID>,
) {
    /**
     * @param id ID of the record
     * @return message to show when record with given ID is not found.
     */
    protected open fun idNotFoundMessage(id: ID): String? =
        "Record $id not found"

    /**
     * Converts entity to response.
     * @param entity entity to convert.
     * @return response dto [RS].
     */
    protected abstract fun toResponse(entity: T): RS

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
        val entity = repository.findById(id).getOrElse {
            throw NotFoundException(idNotFoundMessage(id))
        }

        return ResponseEntity(
            toResponse(entity),
            HttpStatus.OK
        )
    }

    /**
     * Create new record in the database.
     * @param RQ Request type.
     * @param request Request data.
     * @return New record wrapped in [RS] and [ResponseEntity].
     */
    open fun <RQ : EntityDto<T, ID>> create(
        request: RQ
    ): ResponseEntity<RS> {
        val entity = repository.save(request.toEntity())
        return ResponseEntity(
            toResponse(entity),
            HttpStatus.CREATED
        )
    }

    /**
     * Update existing record in the database.
     * @param RQ Request type.
     * @param id ID of the record to update.
     * @param request Request data.
     * @return Updated record wrapped in [RS] and [ResponseEntity].
     */
    open fun <RQ : EntityDto<T, ID>> update(
        id: ID,
        request: RQ
    ): ResponseEntity<RS> {
        repository.findById(id).getOrElse {
            throw NotFoundException(idNotFoundMessage(id))
        }

        val newEntity = repository.save(request.toEntity(id))

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
        val entity = repository.findById(id).getOrElse {
            throw NotFoundException(idNotFoundMessage(id))
        }

        repository.delete(entity)

        return ResponseEntity(
            HttpStatus.OK
        )
    }
}