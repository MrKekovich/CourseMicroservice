package com.mrkekovich.courses.repositories

import com.mrkekovich.courses.models.CourseEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Course repository. Used to perform database operations.
 */
interface CourseRepository: JpaRepository<CourseEntity, String>
