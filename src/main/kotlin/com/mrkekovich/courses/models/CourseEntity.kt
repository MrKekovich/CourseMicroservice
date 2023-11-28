package com.mrkekovich.courses.models

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

/**
 * Course entity
 *
 * @property title Title of a course.
 * @property description Description of a course.
 * @property id (Optional) ID of a course.
 */
@Entity
@Table(name = "courses")
class CourseEntity(
    @Column(nullable = false)
    val title: String?,

    @Column(nullable = false)
    val description: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
) {
    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL])
    var modules: MutableSet<ModuleEntity> = mutableSetOf()
}
