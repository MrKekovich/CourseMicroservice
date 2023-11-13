package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.PhotoDto
import com.mrkekovich.courses.models.PhotoEntity
import com.mrkekovich.courses.repositories.PhotoRepository

class PhotoService(
    photoRepository: PhotoRepository
) : BaseService<PhotoEntity, String, PhotoDto.Response>(
    repository = photoRepository,
) {
    override fun toResponse(entity: PhotoEntity): PhotoDto.Response =
        PhotoDto.Response(entity)
}