package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.EntityInterface

/**
 * Used to easily validate, convert, map and return data.
 * Forces to implement [toEntity] methods
 * and allows to use abstract services.
 *
 * @param T the type of entity that implements [EntityInterface].
 */
abstract class EntityDto<T : EntityInterface<ID>, ID : Any> {
    /**
     * ID of entity.
     */
    open val id: ID? = null

    /**
     * Converts DTO to an entity.
     *
     * @return Entity with [id] (instance field).
     */
    abstract fun toEntity(): T

    /**
     * Converts DTO to an entity with given ID (function parameter).
     *
     * @param id the ID of entity. Leave empty to generate new ID.
     * @return Entity with given ID.
     */
    abstract fun toEntity(id: ID?): T
}