package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseDto
import com.mrkekovich.courses.models.EntityInterface
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.jvm.optionals.getOrNull

abstract class BaseService<T : EntityInterface<ID>, ID : Any>(
    private val repository: JpaRepository<T, ID>,
) {
    abstract fun getAll(): ResponseEntity<out List<BaseDto<T, ID>>>

    abstract fun getById(
        id: ID
    ): ResponseEntity<out BaseDto<T, ID>>

    abstract fun <Request : BaseDto<T, ID>> create(
        dto: Request
    ): ResponseEntity<out BaseDto<T, ID>>

    abstract fun <Request : BaseDto<T, ID>> update(
        id: ID,
        dto: Request
    ): ResponseEntity<out BaseDto<T, ID>>

    open fun delete(id: ID): ResponseEntity<Unit> {
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