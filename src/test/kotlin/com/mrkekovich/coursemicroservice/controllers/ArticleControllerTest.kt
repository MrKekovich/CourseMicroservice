package com.mrkekovich.coursemicroservice.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrkekovich.coursemicroservice.dto.ArticleDto
import com.mrkekovich.coursemicroservice.dto.CreateArticleRequest
import com.mrkekovich.coursemicroservice.dto.DeleteArticleRequest
import com.mrkekovich.coursemicroservice.dto.UpdateArticleRequest
import com.mrkekovich.coursemicroservice.models.ArticleEntity
import com.mrkekovich.coursemicroservice.models.CourseEntity
import com.mrkekovich.coursemicroservice.models.ModuleEntity
import com.mrkekovich.coursemicroservice.repositories.ArticleRepository
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
class ArticleControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val moduleRepository: ModuleRepository,
    val articleRepository: ArticleRepository,
) {
    val baseUrl = "/api/v1/articles"

    private val course = CourseEntity(
        title = "title",
        description = "description",
    )

    @Test
    fun `should create and return article`() {
        // arrange
        val module = moduleRepository.save(getModule(course))
        val request = getArticle(module).toCreateRequest()

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
                jsonPath("$.content") { value(request.content) }
                jsonPath("$.module_id") { value(request.moduleId) }
            }

        validateWithGetRequest(request = request)
    }

    @Test
    fun `should get all articles`() {
        // arrange
        val module = moduleRepository.save(getModule(course))
        val articles = listOf(
            getArticle(module),
            getArticle(module),
        )

        articleRepository.saveAll(articles)

        // act
        mockMvc.get(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { value(articles.size) }
                articles.forEachIndexed { index, article ->
                    jsonPath("$[$index].title") { value(article.title) }
                    jsonPath("$[$index].description") { value(article.description) }
                    jsonPath("$[$index].content") { value(article.content) }
                    jsonPath("$[$index].module_id") { value(article.module?.id) }
                    jsonPath("$[$index].id") { value(article.id) }
                }
            }
    }

    @Test
    fun `should update and return article`() {
        // arrange
        val module = moduleRepository.save(getModule(course))
        val article = articleRepository.save(getArticle(module))
        val request = article.toUpdateRequest()

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
                jsonPath("$.content") { value(request.content) }
                jsonPath("$.module_id") { value(request.moduleId) }
                jsonPath("$.id") { value(article.id) }
            }

        validateWithGetRequest(request = request)
    }

    @Test
    fun `should delete article`() {
        // arrange
        val module = moduleRepository.save(getModule(course))
        val article = articleRepository.save(getArticle(module))
        val request = DeleteArticleRequest(id = article.id)

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

    private fun validateWithGetRequest(
        request: ArticleDto?,
        length: Int = 1,
        index: Int = 0
    ) {
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
                jsonPath("$.length()") { value(length) }
                request?.title?.let { jsonPath("$[$index].title") { value(it) } }
                request?.description?.let { jsonPath("$[$index].description") { value(it) } }
                request?.content?.let { jsonPath("$[$index].content") { value(it) } }
                request?.moduleId?.let { jsonPath("$[$index].module_id") { value(it) } }
                request?.id?.let { jsonPath("$[$index].id") { value(it) } }
            }
    }

    private fun getModule(
        course: CourseEntity,
        parentModule: ModuleEntity? = null
    ): ModuleEntity = ModuleEntity(
        title = "title",
        description = "description",
        position = 0,
        course = course,
        parentModule = parentModule,
    )

    private fun getArticle(
        module: ModuleEntity
    ): ArticleEntity =
        ArticleEntity(
            title = "title",
            description = "description",
            content = "content",
            module = module,
        )

    private fun ArticleEntity.toCreateRequest(): CreateArticleRequest =
        CreateArticleRequest(
            title = title,
            description = description,
            content = content,
            moduleId = module?.id,
        )

    private fun ArticleEntity.toUpdateRequest() =
        UpdateArticleRequest(
            title = title,
            description = description,
            content = content,
            moduleId = module?.id,
            id = id,
        )
}
