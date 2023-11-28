package com.mrkekovich.courses.repositories

import com.mrkekovich.courses.models.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Article JPA repository.
 */
interface ArticleRepository : JpaRepository<ArticleEntity, String>
