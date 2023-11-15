package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.PhotoDto
import com.mrkekovich.courses.services.PhotoService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/photos")
class PhotoController(
    private val photoService: PhotoService
) {
    @PostMapping
    fun upload(
        @Validated
        @RequestBody
        photo: PhotoDto.UploadRequest
    ): ResponseEntity<PhotoDto.Response> =
        photoService.uploadPhoto(photo)
}