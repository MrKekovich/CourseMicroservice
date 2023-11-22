package com.mrkekovich.courses.repositories

import com.mrkekovich.courses.models.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<ArticleEntity, String>
