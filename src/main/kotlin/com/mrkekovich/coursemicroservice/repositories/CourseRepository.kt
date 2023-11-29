package com.mrkekovich.coursemicroservice.repositories

import com.mrkekovich.coursemicroservice.models.CourseEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Course JPA repository.
 */
interface CourseRepository : JpaRepository<CourseEntity, String> {
    @Query(
        """
            SELECT course FROM CourseEntity course
            WHERE 
                (:title IS NULL 
                    OR lower(course.title) LIKE concat('%', lower(:title), '%'))
                AND (:description IS NULL 
                    OR lower(course.description) LIKE concat('%', lower(:description), '%'))
                AND (:id IS NULL 
                    OR course.id = :id)
        """
    )
    fun findAllByFilter(
        @Param("title")
        title: String?,

        @Param("description")
        description: String?,

        @Param("id")
        id: String?,

        pageable: Pageable,
    ): Page<CourseEntity>
}
