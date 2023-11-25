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

class CourseServiceTest {
    private val courseRepository: CourseRepository = mockk()
    private val courseService = CourseService(courseRepository)

    @Test
    fun `should return correct course on create`() {
        // arrange
        val record = CourseEntity("title", "description", "id")
        val request = CreateCourseRequest(record.title, record.description)
        val expected = ResponseEntity(
            record.toBaseResponseDto(),
            HttpStatus.CREATED
        )

        // act
        every {
            courseRepository.save(any())
        } returns record
        val actual = courseService.create(request)

        // assert
        verify(exactly = 1) { courseRepository.save(any()) }
        assert(actual == expected)
    }

    @Test
    fun `should get all courses`() {
        // arrange
        val records = listOf(
            CourseEntity("title", "description"),
            CourseEntity("another title", "another description")
        )
        val expected = ResponseEntity(
            records.map { it.toBaseResponseDto() },
            HttpStatus.OK
        )

        // act
        every {
            courseRepository.findAll()
        } returns records
        val actual = courseService.getAll()

        // assert
        verify(exactly = 1) { courseRepository.findAll() }
        assert(actual == expected)
    }

    @Test
    fun `should return update course`() {
        // arrange
        val record = CourseEntity("title", "description", "id")
        val request = UpdateCourseRequest(record.title, record.description, record.id)
        val expected = ResponseEntity(
            record.toBaseResponseDto(),
            HttpStatus.OK
        )

        // act
        record.id?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns record
        }
        every {
            courseRepository.save(any())
        } returns record
        val actual = courseService.update(request)

        // assert
        verify(exactly = 1) { courseRepository.save(any()) }
        verify(exactly = 1) { courseRepository.findById(any()) }
        assert(actual == expected)
    }

    @Test
    fun `should throw exception if course not found on update`() {
        // arrange
        val record = CourseEntity("title", "description", "id")
        val request = UpdateCourseRequest(record.title, record.description, record.id)

        // act
        record.id?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            courseRepository.save(any())
        } returns record

        // assert
        assertThrows<NotFoundException> {
            courseService.update(request)
        }
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 0) { courseRepository.save(any()) }
    }

    @Test
    fun `should delete course`() {
        // arrange
        val record = CourseEntity("title", "description", "id")
        val request = DeleteCourseRequest(record.id)
        val expected = ResponseEntity<HttpStatus>(HttpStatus.OK)

        // act
        record.id?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns record
        }
        every {
            courseRepository.delete(any())
        } returns Unit
        val actual = courseService.delete(request)

        // assert
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 1) { courseRepository.delete(any()) }
        assert(actual == expected)
    }

    @Test
    fun `should throw exception if course not found on delete`() {
        // arrange
        val record = CourseEntity("title", "description", "id")
        val request = DeleteCourseRequest(record.id)

        // act
        record.id?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            courseRepository.delete(any())
        } returns Unit

        // assert
        assertThrows<NotFoundException> {
            courseService.delete(request)
        }
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 0) { courseRepository.delete(any()) }
    }
}
