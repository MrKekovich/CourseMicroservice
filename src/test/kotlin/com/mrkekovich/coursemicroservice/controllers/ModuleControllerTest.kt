package com.mrkekovich.coursemicroservice.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrkekovich.coursemicroservice.dto.CreateModuleRequest
import com.mrkekovich.coursemicroservice.dto.DeleteModuleRequest
import com.mrkekovich.coursemicroservice.dto.ModuleDto
import com.mrkekovich.coursemicroservice.dto.UpdateModuleRequest
import com.mrkekovich.coursemicroservice.models.CourseEntity
import com.mrkekovich.coursemicroservice.models.ModuleEntity
import com.mrkekovich.coursemicroservice.repositories.CourseRepository
import com.mrkekovich.coursemicroservice.repositories.ModuleRepository
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
internal class ModuleControllerTest @Autowired constructor(
    val courseRepository: CourseRepository,
    val moduleRepository: ModuleRepository,
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
) {
    private val baseUrl = "/api/v1/modules"

    private val course = CourseEntity(
        title = "title",
        description = "description",
    )

    @Test
    fun `should create and return module`() {
        // arrange
        val course = courseRepository.save(course)
        val request = getModule(course = course).toCreateRequest()

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
                jsonPath("$.position") { value(request.position) }
                jsonPath("$.course_id") { value(request.courseId) }
                jsonPath("$.parent_module_id") { value(request.parentModuleId) }
            }

        validateWithGetRequest(request = request)
    }

    @Test
    fun `should create and return nested module`() {
        // arrange
        val module = moduleRepository.save(getModule(course))
        val request = getModule(
            course = course,
            parentModule = module
        ).toCreateRequest()

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
                jsonPath("$.position") { value(request.position) }
                jsonPath("$.course_id") { value(request.courseId) }
                jsonPath("$.parent_module_id") { value(request.parentModuleId) }
            }

        validateWithGetRequest(
            request = request,
            length = 2,
            index = 1
        )
    }

    @Test
    fun `should return all modules`() {
        // arrange
        val records = listOf(
            getModule(course = course),
            getModule(course = course)
        )

        moduleRepository.saveAll(records)

        // act
        mockMvc.get(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            // assert
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { value(records.size) }
                records.forEachIndexed { index, module ->
                    jsonPath("$[$index].title") { value(module.title) }
                    jsonPath("$[$index].description") { value(module.description) }
                    jsonPath("$[$index].position") { value(module.position) }
                    jsonPath("$[$index].course_id") { value(module.course?.id) }
                    jsonPath("$[$index].parent_module_id") { value(module.parentModule?.id) }
                }
            }
    }

    @Test
    fun `should update and return module`() {
        // arrange
        val module = moduleRepository.save(getModule(course = course))
        val request = module.toUpdateRequest()

        // act
        mockMvc.patch(baseUrl) {
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
                jsonPath("$.position") { value(request.position) }
                jsonPath("$.course_id") { value(request.courseId) }
                jsonPath("$.parent_module_id") { value(request.parentModuleId) }
                jsonPath("$.id") { value(module.id) }
            }

        validateWithGetRequest(request = request)
    }

    @Test
    fun `should delete module`() {
        // arrange
        val module = moduleRepository.save(getModule(course = course))
        val request = DeleteModuleRequest(id = module.id)

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
        request: ModuleDto?,
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
                request?.position?.let { jsonPath("$[$index].position") { value(it) } }
                request?.courseId?.let { jsonPath("$[$index].course_id") { value(it) } }
                request?.parentModuleId?.let { jsonPath("$[$index].parent_module_id") { value(it) } }
                request?.id?.let { jsonPath("$[$index].id") { value(it) } }
            }
    }

    private fun getModule(
        course: CourseEntity?,
        parentModule: ModuleEntity? = null,
    ): ModuleEntity =
        ModuleEntity(
            title = "title",
            description = "description",
            position = 0,
            course = course,
            parentModule = parentModule,
        )

    private fun ModuleEntity.toCreateRequest(): CreateModuleRequest =
        CreateModuleRequest(
            title = title,
            description = description,
            position = position,
            parentModuleId = parentModule?.id,
            courseId = course?.id,
        )

    private fun ModuleEntity.toUpdateRequest(): UpdateModuleRequest =
        UpdateModuleRequest(
            title = "new title",
            description = "new description",
            position = position,
            parentModuleId = parentModule?.id,
            courseId = course?.id,
            id = id
        )
}
