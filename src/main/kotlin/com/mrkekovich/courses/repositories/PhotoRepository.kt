package com.mrkekovich.courses.repositories

import com.mrkekovich.courses.models.PhotoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<PhotoEntity, String>