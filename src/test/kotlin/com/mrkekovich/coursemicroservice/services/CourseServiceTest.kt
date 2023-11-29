package com.mrkekovich.coursemicroservice.services

import com.mrkekovich.coursemicroservice.dto.CreateCourseRequest
import com.mrkekovich.coursemicroservice.dto.DeleteCourseRequest
import com.mrkekovich.coursemicroservice.dto.GetCoursesRequest
import com.mrkekovich.coursemicroservice.dto.UpdateCourseRequest
import com.mrkekovich.coursemicroservice.exceptions.NotFoundException
import com.mrkekovich.coursemicroservice.mappers.toBaseResponseDto
import com.mrkekovich.coursemicroservice.models.CourseEntity
import com.mrkekovich.coursemicroservice.repositories.CourseRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
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
        val expected = course.toBaseResponseDto()

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
        val records = PageImpl(listOf(course, course))
        val expected = records.map { it.toBaseResponseDto() }
        val dto = GetCoursesRequest()

        every {
            courseRepository.findAllByFilter(
                title = dto.title,
                description = dto.description,
                id = dto.id,
                pageable = any()
            )
        } returns records

        // act
        val actual = courseService.getAll(dto)

        // assert
        assert(actual == expected)
        verify(exactly = 1) {
            courseRepository.findAllByFilter(
                title = any(),
                description = any(),
                id = any(),
                pageable = any(),
            )
        }
    }

    @Test
    fun `should return updated course`() {
        // arrange
        val request = UpdateCourseRequest(
            title = course.title,
            description = course.description,
            id = course.id
        )
        val expected = course.toBaseResponseDto()

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
        val expected = Unit

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
