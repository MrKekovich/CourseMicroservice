package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.PhotoEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * @property fileName File name of a photo.
 */
sealed class PhotoDto : BaseDto<PhotoEntity, String>() {
    @get:Length(min = 5, max = 100)
    @get:NotNull
    @get:NotBlank
    abstract val fileName: String?

    override fun toEntity(): PhotoEntity {
        return PhotoEntity(
            fileName = fileName,
            id = id
        )
    }

    override fun toEntity(id: String?): PhotoEntity {
        return PhotoEntity(
            fileName = fileName,
            id = id
        )
    }

    data class Response(
        override val fileName: String?,

        @get:NotNull
        @get:NotBlank
        override val id: String?
    ) : PhotoDto() {
        constructor(entity: PhotoEntity) : this(
            fileName = entity.fileName,
            id = entity.id
        )
    }

    data class Request(
        override val fileName: String?,

        @get:NotNull
        @get:NotBlank
        override val id: String?
    ) : PhotoDto()
}