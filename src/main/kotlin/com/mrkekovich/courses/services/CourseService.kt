package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository
) {

    fun getAll(): List<CourseDto.Response> {
        return courseRepository.findAll().map {
            CourseDto.Response(it)
        }
    }

    fun getById(id: String): CourseDto.Response {
        val entity = courseRepository.findById(id).get()
        return CourseDto.Response(entity)
    }

    fun create(course: CourseDto): CourseDto.Response {
        val entity = CourseEntity(
            name = course.name,
            description = course.description
        )
        courseRepository.save(entity)
        return CourseDto.Response(entity)
    }

    fun update(
        id: String,
        course: CourseDto.Request,
    ): CourseDto.Response {
        val entity = courseRepository.findById(id).get()
        courseRepository.save(
            CourseEntity(
                id = entity.id,
                name = course.name,
                description = course.description
            )
        )
        return CourseDto.Response(entity)
    }

    fun delete(id: String) {
        courseRepository.deleteById(id)
    }

}
