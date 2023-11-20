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
    @Value("\${app.photos.allowed.extensions}")
    private val photoRepository: PhotoRepository
) {
    @Transactional
    fun upload(
        photo: PhotoDto.Request.Upload
    ): ResponseEntity<PhotoDto.Response.Base> {
        val fileName = UUID.randomUUID().toString()

        val createdFile = photo.file?.let {
            saveFile(name = fileName, file = it)
        } ?: return ResponseEntity( // Shouldn't happen because photo is validated in DTO.
            HttpStatus.BAD_REQUEST
        )

        val entity = photoRepository.save(
            PhotoEntity(
                fileName = createdFile.name,
                id = fileName
            )
        )

        return ResponseEntity(
            entity.toResponse(),
            HttpStatus.OK
        )
    }

    private fun saveFile(name: String, file: MultipartFile): File? {
        val extension = file.originalFilename?.split(".")?.last()
            ?: return null

        return File("$name.$extension").apply {
            file.transferTo(this)
            createNewFile()
        }
    }
}

private fun PhotoEntity.toResponse(): PhotoDto.Response.Base {
    return PhotoDto.Response.Base(
        id = id,
        fileName = fileName
    )
}
