package com.mrkekovich.coursemicroservice.repositories

import com.mrkekovich.coursemicroservice.models.ArticleEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Article JPA repository.
 */
interface ArticleRepository : JpaRepository<ArticleEntity, String> {
    @Query(
        """
            SELECT article FROM ArticleEntity article
            WHERE (article.title LIKE concat('%', ?1, '%')) 
                AND (article.description LIKE concat('%', ?2, '%'))
                AND (article.content LIKE concat('%', ?3, '%'))
                AND (article.module = ?4 )
                AND (article.id = ?5)
        """
    )
    fun findAllByTitleContainingAndDescriptionContainingAndContentContainingAndModuleAndId(
        @Param("title")
        title: String?,

        @Param("description")
        description: String?,

        @Param("content")
        content: String?,

        @Param("module")
        moduleId: String?,

        @Param("id")
        id: String?,
        pageable: Pageable,
    ): Page<ArticleEntity>
}
