package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.PhotoDto
import com.mrkekovich.courses.models.PhotoEntity
import com.mrkekovich.courses.repositories.PhotoRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
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
        photo: PhotoDto.UploadRequest,
    ): ResponseEntity<PhotoDto.Response> {
        val fileName = UUID.randomUUID().toString()

        val createdFile = photo.file?.let {
            saveFile(name = fileName, file = it)
        } ?: return ResponseEntity(  // Shouldn't happen because photo is validated in DTO.
            HttpStatus.BAD_REQUEST
        )

        val entity = photoRepository.save(
            PhotoEntity(
                fileName = createdFile.name,
                id = fileName
            )
        )

        return ResponseEntity(
            PhotoDto.Response(entity = entity),
            HttpStatus.OK
        )
    }

    /**
     * Save file to storage.
     * @param name
     * @param file
     * @return
     */
    private fun saveFile(
        name: String,
        file: MultipartFile,
    ): File? {
        val fileExtension = file.originalFilename?.let { File(it).extension }
            ?: return null

        return File("$photosStorageLocation/$name.$fileExtension").apply {
            writeBytes(file.bytes)
            createNewFile()
        }
    }
}