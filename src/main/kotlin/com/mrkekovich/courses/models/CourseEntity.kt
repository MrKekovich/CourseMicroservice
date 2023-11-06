package com.mrkekovich.courses.models

import jakarta.persistence.*

@Entity
@Table(name = "courses")
data class CourseEntity (
    @Column(nullable = false)
    val name: String?,

    @Column(nullable = false)
    val description: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
)
