package com.mrkekovich.courses.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.dto.CreateCourseRequest
import com.mrkekovich.courses.dto.DeleteCourseRequest
import com.mrkekovich.courses.dto.GetCoursesRequest
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

    @Test
    fun `should create and return course`() {
        // arrange
        val request = getCourse().toCreateRequest()

        // act
        mockMvc.post(baseUrl) {
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
            }

        validateWithGetRequest(request = request)
    }

    @Test
    fun `should return courses`() {
        // arrange
        val expected = listOf(
            getCourse(),
            getCourse()
        )
        courseRepository.saveAll(expected)
        val request = GetCoursesRequest()

        // act
        mockMvc.get(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.content.length()") { value(expected.size) }
                expected.forEachIndexed { index, course ->
                    jsonPath("$.content[$index].title") { value(course.title) }
                    jsonPath("$.content[$index].description") { value(course.description) }
                }
            }
    }

    @Test
    fun `should filter and return courses`() {
        // arrange
        val uniqueCourse = CourseEntity(
            title = "unique title",
            description = "unique description",
        )
        val expected = listOf(
            getCourse(),
            getCourse(),
            uniqueCourse,
        )
        courseRepository.saveAll(expected)
        val request = GetCoursesRequest(title = "unique")

        // act
        mockMvc.get(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.content.length()") { value(1) }
                jsonPath("$.content[0].title") { value(uniqueCourse.title) }
                jsonPath("$.content[0].description") { value(uniqueCourse.description) }
            }
    }

    @Test
    fun `should update course`() {
        // arrange
        val course = courseRepository.save(getCourse())

        val request = course.toUpdateRequest()

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
        val course = courseRepository.save(getCourse())

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
        index: Int = 0,
        getCoursesRequest: GetCoursesRequest? = null,
    ) {
        mockMvc.get(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(getCoursesRequest ?: GetCoursesRequest())
        }
            .andExpect {
                jsonPath("$.content.length()") { value(length) }
                request?.title?.let { jsonPath("$.content[$index].title") { value(it) } }
                request?.description?.let { jsonPath("$.content[$index].description") { value(it) } }
                request?.id?.let { jsonPath("$.content[$index].id") { value(it) } }
            }
    }

    private fun getCourse() =
        CourseEntity(
            title = "title",
            description = "description",
        )

    private fun CourseEntity.toCreateRequest() =
        CreateCourseRequest(
            title = title,
            description = description,
        )

    private fun CourseEntity.toUpdateRequest() =
        UpdateCourseRequest(
            title = "new title",
            description = "new description",
            id = id,
        )
}
