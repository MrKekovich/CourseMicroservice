package com.mrkekovich.courses.utils

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

class PhotoFileManager(
    private val file: MultipartFile?,

    private val photosStorageLocation: String,

    private val photosAllowedExtensions: String
) {
    val extension: String?
        get() {
        val fileName = file?.originalFilename

        val fileExtension = fileName?.let { File(it).extension }
            ?: "empty"

        if (!photosAllowedExtensions.split(",").contains(fileExtension)) {
            return null
        }

        return fileExtension
    }

    fun saveFile(fileName: UUID?, fileExtension: String?): File {
        val name = fileName ?: file?.originalFilename
        val extension = fileExtension ?: extension

        return File("$photosStorageLocation/$name.$extension").apply {
            file?.bytes?.let { writeBytes(it) }
            createNewFile()
        }
    }

}