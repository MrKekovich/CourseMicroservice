package com.mrkekovich.courses.models

import jakarta.persistence.*

/**
 * Course entity
 * @property title Title of a course.
 * @property description Description of a course.
 * @property id (Optional) ID of a course.
 */
@Entity
@Table(name = "courses")
data class CourseEntity (
    @Column(nullable = false)
    val title: String?,

    @Column(nullable = false)
    val description: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override val id: String? = null,
) : BaseEntity()
