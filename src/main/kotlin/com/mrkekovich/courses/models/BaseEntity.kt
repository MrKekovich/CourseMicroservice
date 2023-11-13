package com.mrkekovich.courses.models

/**
 * Base entity forces child classes to have [id] of type `String?`
 * and allows to use abstract DTOs and services.
 */
abstract class BaseEntity {
    abstract val id: String?
}