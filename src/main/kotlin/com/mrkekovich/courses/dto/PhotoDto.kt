package com.mrkekovich.courses.dto

import com.mrkekovich.courses.models.PhotoEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

/**
 * @property fileName File name of a photo.
 */
sealed class PhotoDto : BaseDto<PhotoEntity, String>() {
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
        override val fileName: String?
    ) : PhotoDto()

    data class UploadRequest(
        @NotNull
        @NotBlank
        val file: MultipartFile?,
    ) : PhotoDto() {
        override val fileName: String?
            get() = file?.originalFilename

        fun toEntity(fileName: String): PhotoEntity {
            return PhotoEntity(
                fileName = fileName,
                id = id,
            )
        }

        fun toEntity(
            id: String?,
            fileName: String
        ): PhotoEntity {
            return PhotoEntity(
                fileName = fileName,
                id = id,
            )
        }
    }
}