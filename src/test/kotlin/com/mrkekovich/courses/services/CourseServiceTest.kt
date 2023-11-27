package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CreateCourseRequest
import com.mrkekovich.courses.dto.DeleteCourseRequest
import com.mrkekovich.courses.dto.UpdateCourseRequest
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.mappers.toBaseResponseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.jvm.optionals.getOrNull

internal class CourseServiceTest {
    private val courseRepository: CourseRepository = mockk()

    private val courseService = CourseService(courseRepository)

    private val course = CourseEntity(
        title = "title",
        description = "description",
        id = "id"
    )

    @Test
    fun `should return correct course on create`() {
        // arrange
        val request = CreateCourseRequest(
            title = course.title,
            description = course.description
        )
        val expected = ResponseEntity(
            course.toBaseResponseDto(),
            HttpStatus.CREATED
        )
        every {
            courseRepository.save(any())
        } returns course

        // act
        val actual = courseService.create(request)

        // assert
        verify(exactly = 1) { courseRepository.save(any()) }
        assert(actual == expected)
    }

    @Test
    fun `should get all courses`() {
        // arrange
        val records = listOf(course, course)
        val expected = ResponseEntity(
            records.map { it.toBaseResponseDto() },
            HttpStatus.OK
        )

        every {
            courseRepository.findAll()
        } returns records

        // act
        val actual = courseService.getAll()

        // assert
        assert(actual == expected)
        verify(exactly = 1) { courseRepository.findAll() }
    }

    @Test
    fun `should return updated course`() {
        // arrange
        val request = UpdateCourseRequest(
            title = course.title,
            description = course.description,
            id = course.id
        )
        val expected = ResponseEntity(
            course.toBaseResponseDto(),
            HttpStatus.OK
        )

        request.id?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns course
        }
        every {
            courseRepository.save(any())
        } returns course

        // act
        val actual = courseService.update(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { courseRepository.save(any()) }
        verify(exactly = 1) { courseRepository.findById(any()) }
    }

    @Test
    fun `should throw exception if course not found on update`() {
        // arrange
        val request = UpdateCourseRequest(
            title = course.title,
            description = course.description,
            id = course.id
        )
        val expectedMessage = "Course with id \"${request.id}\" not found"

        request.id?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            courseRepository.save(any())
        } returns course

        // act
        val message = assertThrows<NotFoundException> {
            courseService.update(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 0) { courseRepository.save(any()) }
    }

    @Test
    fun `should delete course`() {
        // arrange
        val request = DeleteCourseRequest(course.id)
        val expected = ResponseEntity<HttpStatus>(HttpStatus.OK)

        request.id?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns course
        }
        every {
            courseRepository.delete(any())
        } returns Unit

        // act
        val actual = courseService.delete(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 1) { courseRepository.delete(any()) }
    }

    @Test
    fun `should throw exception if course not found on delete`() {
        // arrange
        val request = DeleteCourseRequest(course.id)
        val expectedMessage = "Course with id \"${request.id}\" not found"

        request.id?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            courseRepository.delete(any())
        } returns Unit

        // act
        val message = assertThrows<NotFoundException> {
            courseService.delete(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 0) { courseRepository.delete(any()) }
    }
}
