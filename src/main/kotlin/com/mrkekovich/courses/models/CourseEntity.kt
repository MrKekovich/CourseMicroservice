package com.mrkekovich.courses.models

import jakarta.persistence.*
import lombok.Builder

@Entity
@Builder
data class CourseEntity (
    @Column(nullable = false)
    val name: String?,

    @Column(nullable = false)
    val description: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
)
