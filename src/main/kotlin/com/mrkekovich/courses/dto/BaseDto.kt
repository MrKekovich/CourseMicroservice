package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.EntityInterface

/**
 * Used to easily validate, convert, map and return data.
 * Forces to implement [toEntity] methods
 * and allows to use abstract services.
 * @param T the type of entity that implements [EntityInterface]
 */
abstract class BaseDto<T : EntityInterface<ID>, ID : Any> {
    /**
     * This field allows you to focus only
     * on important parts of the DTO.
     */
    open val id: ID? = null

    /**
     * Converts DTO to an entity.
     * @return Entity with [id] field.
     */
    abstract fun toEntity(): T

    /**
     * Converts DTO to an entity with given ID.
     * @param id the ID of entity. Leave empty to generate new ID.
     * @return Entity with given ID.
     */
    abstract fun toEntity(id: ID?): T
}