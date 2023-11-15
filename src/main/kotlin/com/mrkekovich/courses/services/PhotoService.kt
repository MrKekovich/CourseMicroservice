package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.PhotoDto
import com.mrkekovich.courses.repositories.PhotoRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import java.io.File
import java.util.*

@Service
class PhotoService(
    @Value("\${app.photos.storage.location}")
    private val photosStorageLocation: String,

    private val photoRepository: PhotoRepository,
) {
    @Transactional
    fun uploadPhoto(
        @Validated
        @RequestBody
        photo: PhotoDto.UploadRequest
    ): ResponseEntity<PhotoDto.Response> {
        val fileExtension = photo.file?.originalFilename?.let {
            File(it).extension
        }

        val fileName = "${UUID.randomUUID()}.${fileExtension}"
        val file = File("$photosStorageLocation/$fileName").apply {
            photo.file?.transferTo(this)
            createNewFile()
        }

        val entity = photoRepository.save(
            photo.toEntity(fileName = fileName)
        )

        return ResponseEntity(
            PhotoDto.Response(entity = entity),
            HttpStatus.OK
        )

    }
}