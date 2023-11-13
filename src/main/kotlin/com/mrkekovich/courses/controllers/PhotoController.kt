package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.PhotoDto
import com.mrkekovich.courses.models.PhotoEntity
import com.mrkekovich.courses.services.PhotoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/photos")
class PhotoController(
    private val photoService: PhotoService
) : BaseController<PhotoEntity, String, PhotoDto.Response, PhotoDto.Request>() {
    @GetMapping
    override fun getAll(): ResponseEntity<List<PhotoDto.Response>> =
        photoService.getAll()

    @GetMapping("/{id}")
    override fun getById(
        @PathVariable id: String
    ): ResponseEntity<PhotoDto.Response> =
        photoService.getById(id)

    @PostMapping
    override fun create(
        @Validated
        @RequestBody
        dto: PhotoDto.Request
    ): ResponseEntity<PhotoDto.Response> =
        photoService.create(dto)

    @PatchMapping("/{id}")
    override fun update(
        @Validated
        @PathVariable
        id: String,

        @Validated
        @RequestBody
        dto: PhotoDto.Request
    ): ResponseEntity<PhotoDto.Response> =
        photoService.update(id, dto)

    @DeleteMapping("/{id}")
    override fun delete(
        @Validated
        @PathVariable
        id: String
    ): ResponseEntity<HttpStatus> =
        photoService.delete(id)
}