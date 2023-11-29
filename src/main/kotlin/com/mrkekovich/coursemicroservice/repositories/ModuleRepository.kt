package com.mrkekovich.coursemicroservice.repositories

import com.mrkekovich.coursemicroservice.models.ModuleEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Module JPA repository
 */
interface ModuleRepository : JpaRepository<ModuleEntity, String>
