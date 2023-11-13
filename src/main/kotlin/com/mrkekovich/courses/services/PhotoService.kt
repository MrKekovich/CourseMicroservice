package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseDto
import com.mrkekovich.courses.dto.PhotoDto
import com.mrkekovich.courses.models.PhotoEntity
import com.mrkekovich.courses.repositories.PhotoRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PhotoService(
    photoRepository: PhotoRepository
) : BaseService<PhotoEntity, String, PhotoDto.Response>(
    repository = photoRepository,
) {
    override fun toResponse(entity: PhotoEntity): PhotoDto.Response =
        PhotoDto.Response(entity)

    @Transactional
    override fun <RQ : BaseDto<PhotoEntity, String>> create(dto: RQ): ResponseEntity<PhotoDto.Response> {
        val response = super.create(dto)
        // TODO: save photo
        return response
    }

    @Transactional
    override fun delete(id: String): ResponseEntity<HttpStatus> {
        // TODO: delete photo
        return super.delete(id)
    }
}