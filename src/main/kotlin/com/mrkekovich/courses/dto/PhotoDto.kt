package com.mrkekovich.courses.dto

import com.mrkekovich.courses.annotations.AllowedExtensions
import com.mrkekovich.courses.models.PhotoEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

/**
 * @property fileName File name of a photo.
 * Validated by
 * [NotNull],
 * [NotBlank]
 */
sealed class PhotoDto : EntityDto<PhotoEntity, String>() {
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

    /**
     * For documentation see [PhotoDto].
     *
     * @property id Photo id.
     * Validated by
     * [NotNull],
     * [NotBlank].
     */
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

    /**
     * Used to upload photo.
     *
     * @property file [MultipartFile] which contains uploaded file.
     * Validated by
     * [AllowedExtensions] ("png", "jpg", "jpeg"),
     * [NotNull]
     */
    data class UploadRequest(
        @get:NotNull
        @get:AllowedExtensions(["png", "jpg", "jpeg"])
        val file: MultipartFile?,
    ) : PhotoDto() {
        override val fileName: String?
            get() = file?.originalFilename
    }
}