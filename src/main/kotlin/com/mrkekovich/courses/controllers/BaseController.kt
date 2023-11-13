package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.BaseDto
import com.mrkekovich.courses.models.EntityInterface
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

abstract class BaseController<
        T : EntityInterface<ID>,
        ID : Any,
        RS : BaseDto<T, ID>,
        RQ : BaseDto<T, ID>> {
    /**
     * Get all records from the database.
     * @return List of records wrapped in [RS] and [ResponseEntity].
     */
    abstract fun getAll(): ResponseEntity<List<RS>>

    /**
     * Get record by id from the database.
     * @param id [ID] of the record to get.
     * @return Record wrapped in [RS] and [ResponseEntity].
     */
    abstract fun getById(
        @Validated
        @PathVariable
        id: ID
    ): ResponseEntity<RS>

    /**
     * Create new record in the database.
     * @param dto Request data of type [RQ].
     * @return Created record wrapped in [RS] and [ResponseEntity].
     */
    abstract fun create(
        @Validated
        @RequestBody
        dto: RQ
    ): ResponseEntity<RS>

    /**
     * Update existing record in the database.
     * @param id [ID] of the record to update.
     * @param dto Request data of type [RQ].
     * @return Updated record wrapped in [RS] and [ResponseEntity].
     */
    abstract fun update(
        id: ID,

        dto: RQ
    ): ResponseEntity<RS>

    /**
     * Delete existing record in the database.
     * @param id [ID] of the record to delete.
     * @return [HttpStatus] wrapped in [ResponseEntity].
     */
    abstract fun delete(
        id: ID
    ): ResponseEntity<HttpStatus>
}