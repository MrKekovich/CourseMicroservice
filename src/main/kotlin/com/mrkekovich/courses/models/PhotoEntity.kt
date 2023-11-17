package com.mrkekovich.courses.models

import jakarta.persistence.*


/**
 * Photo entity
 * @property fileName File name of a photo.
 * @property id ID of a photo.
 */
@Entity
@Table(name = "photos")
data class PhotoEntity(
    @Column(name = "file_name")
    val fileName: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    override val id: String? = null
) : EntityInterface<String>