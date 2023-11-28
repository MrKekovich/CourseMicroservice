package com.mrkekovich.courses.repositories

import com.mrkekovich.courses.models.ModuleEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Module JPA repository
 */
interface ModuleRepository : JpaRepository<ModuleEntity, String>
