package com.mrkekovich.courses.models

import jakarta.persistence.*

/**
 * Photo entity
 * @property fileName File name of a photo.
 * @property id (Optional) ID of a photo.
 */
@Entity
@Table(name = "photos")
data class PhotoEntity(
    @Column(name = "file_name", nullable = false)
    val fileName: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null
)