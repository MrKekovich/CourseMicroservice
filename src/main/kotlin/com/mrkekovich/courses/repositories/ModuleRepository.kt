package com.mrkekovich.courses.repositories

import com.mrkekovich.courses.models.ModuleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ModuleRepository : JpaRepository<ModuleEntity, String>
