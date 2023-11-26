package com.mrkekovich.courses.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrkekovich.courses.dto.CreateCourseRequest
import com.mrkekovich.courses.dto.DeleteCourseRequest
import com.mrkekovich.courses.dto.UpdateCourseRequest
import com.mrkekovich.courses.models.CourseEntity
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
    val objectMapper: ObjectMapper
) {
    private val course = CourseEntity(
        title = "title",
        description = "description",
        id = "id"
    )
    private val request = CreateCourseRequest(
        title = course.title,
        description = course.description
    )

    @Test
    fun `should create and return course`() {
        // act
        mockMvc.post("/api/v1/courses") {
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
    }

    @Test
    fun `should return all courses`() {
        // arrange
        val expected = listOf(request, request)

        expected.forEach {
            mockMvc.post("/api/v1/courses") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(it)
            }
        }

        // act
        mockMvc.get("/api/v1/courses") {
            contentType = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { value(expected.size) }
                jsonPath("$[0].title") { value(expected[0].title) }
                jsonPath("$[0].description") { value(expected[0].description) }
                jsonPath("$[1].title") { value(expected[1].title) }
                jsonPath("$[1].description") { value(expected[1].description) }
            }
    }

    @Test
    fun `should update course`() {
        // arrange
        val createResult = mockMvc.post("/api/v1/courses") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andReturn()

        val createdCourseId = objectMapper.readTree(createResult.response.contentAsString).get("id").asText()

        val updateRequest = UpdateCourseRequest(
            title = "new title",
            description = "new description",
            id = createdCourseId
        )

        // act
        mockMvc.patch("/api/v1/courses", updateRequest.id) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateRequest)
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.title") { value(updateRequest.title) }
                jsonPath("$.description") { value(updateRequest.description) }
                jsonPath("$.id") { value(updateRequest.id) }
            }

        mockMvc.get("/api/v1/courses") {
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                jsonPath("$.length()") { value(1) }
                jsonPath("$[0].id") { value(updateRequest.id) }
                jsonPath("$[0].title") { value(updateRequest.title) }
                jsonPath("$[0].description") { value(updateRequest.description) }
            }
    }

    @Test
    fun `should delete course`() {
        // arrange
        val createResult = mockMvc.post("/api/v1/courses") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andReturn()

        val createdCourseId = objectMapper.readTree(createResult.response.contentAsString).get("id").asText()

        val request = DeleteCourseRequest(
            id = createdCourseId
        )

        // act
        mockMvc.delete("/api/v1/courses", createdCourseId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
            }

        mockMvc.get("/api/v1/courses") {
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { value(0) }
            }
    }
}
