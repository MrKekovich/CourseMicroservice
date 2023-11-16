package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.PhotoDto
import com.mrkekovich.courses.services.PhotoService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/photos")
class PhotoController(
    private val photoService: PhotoService
) {
    @PostMapping(
        "/upload",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun upload(
        @Validated
        @ModelAttribute
        file: PhotoDto.UploadRequest
    ): ResponseEntity<PhotoDto.Response> =
        photoService.uploadPhoto(file = file)
}