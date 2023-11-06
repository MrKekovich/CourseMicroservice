package com.mrkekovich.courses.repositories

import com.mrkekovich.courses.models.CourseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CourseRepository: JpaRepository<CourseEntity, String>
