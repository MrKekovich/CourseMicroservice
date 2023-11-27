package com.mrkekovich.courses.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.dto.CreateCourseRequest
import com.mrkekovich.courses.dto.DeleteCourseRequest
import com.mrkekovich.courses.dto.UpdateCourseRequest
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = ["classpath:application-test.properties"])
internal class CourseControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val courseRepository: CourseRepository,
) {
    private val baseUrl = "/api/v1/courses"

    private val course = CourseEntity(
        title = "title",
        description = "description",
    )
    private val request = CreateCourseRequest(
        title = course.title,
        description = course.description
    )

    @Test
    fun `should create and return course`() {
        // act
        mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.title") { value(course.title) }
                jsonPath("$.description") { value(course.description) }
            }

        validateWithGetRequest(request = request)
    }

    @Test
    fun `should return all courses`() {
        // arrange
        val expected = listOf(course, course)
        courseRepository.saveAll(expected)

        // act
        mockMvc.get(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { value(expected.size) }
                expected.forEachIndexed { index, course ->
                    jsonPath("$[$index].title") { value(course.title) }
                    jsonPath("$[$index].description") { value(course.description) }
                }
            }
    }

    @Test
    fun `should update course`() {
        // arrange
        val course = courseRepository.save(course)

        val request = UpdateCourseRequest(
            title = "new title",
            description = "new description",
            id = course.id
        )

        // act
        mockMvc.patch(baseUrl, request.id) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.title") { value(request.title) }
                jsonPath("$.description") { value(request.description) }
                jsonPath("$.id") { value(request.id) }
            }

        validateWithGetRequest(request = request)
    }

    @Test
    fun `should delete course`() {
        // arrange
        val course = courseRepository.save(course)

        val request = DeleteCourseRequest(id = course.id)

        // act
        mockMvc.delete(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
            }

        validateWithGetRequest(request = null, length = 0)
    }

    fun validateWithGetRequest(
        request: CourseDto?,
        length: Int = 1,
        index: Int = 0
    ) {
        mockMvc.get(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                jsonPath("$.length()") { value(length) }
                request?.title?.let { jsonPath("$[$index].title") { value(it) } }
                request?.description?.let { jsonPath("$[$index].description") { value(it) } }
                request?.id?.let { jsonPath("$[$index].id") { value(it) } }
            }
    }
}
