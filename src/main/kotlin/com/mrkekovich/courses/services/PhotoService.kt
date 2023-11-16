package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.PhotoDto
import com.mrkekovich.courses.repositories.PhotoRepository
import com.mrkekovich.courses.utils.PhotoFileManager
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

    @Value("\${app.photos.allowed.extensions}")
    private val photosAllowedExtensions: String,

    private val photoRepository: PhotoRepository,
) {
    @Transactional
    fun uploadPhoto(
        @Validated
        @RequestBody
        file: PhotoDto.UploadRequest
    ): ResponseEntity<PhotoDto.Response> {
        val fileManager = PhotoFileManager(
            file = file.file,
            photosAllowedExtensions = photosAllowedExtensions,
            photosStorageLocation = photosStorageLocation
        )

        val fileExtension = fileManager.extension
            ?: return ResponseEntity<PhotoDto.Response>(
                HttpStatus.BAD_REQUEST
            )

        val fileName = UUID.randomUUID()

        fileManager.saveFile(fileName, fileExtension)

        val entity = photoRepository.save(
            file.toEntity(
                fileName = "$fileName.$fileExtension",
                id = fileName.toString()
            )
        )

        return ResponseEntity(
            PhotoDto.Response(entity = entity),
            HttpStatus.OK
        )
    }

    private fun validateExtension(file: PhotoDto.UploadRequest): String {
        val fileName = file.file?.originalFilename
            ?: throw IllegalArgumentException("File name required")
        val extension = File(fileName).extension

        if (!photosAllowedExtensions.split(",").contains(extension)) {
            throw IllegalArgumentException("Extension $extension is not allowed")
        }
        return extension
    }
}