package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CreateArticleRequest
import com.mrkekovich.courses.dto.DeleteArticleRequest
import com.mrkekovich.courses.dto.UpdateArticleRequest
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.mappers.toBaseResponseDto
import com.mrkekovich.courses.models.ArticleEntity
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.models.ModuleEntity
import com.mrkekovich.courses.repositories.ArticleRepository
import com.mrkekovich.courses.repositories.ModuleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.jvm.optionals.getOrNull

internal class ArticleServiceTest {
    private val articleRepository: ArticleRepository = mockk()
    private val moduleRepository: ModuleRepository = mockk()

    private val articleService = ArticleService(articleRepository, moduleRepository)

    private val course = CourseEntity(
        title = "title",
        description = "description",
        id = "id"
    )
    private val module = ModuleEntity(
        title = "title",
        description = "description",
        course = course,
        id = "id"
    )
    private val article = ArticleEntity(
        title = "title",
        description = "description",
        content = "content",
        module = module,
        id = "id"
    )

    @Test
    fun `should return correct article on create`() {
        // arrange
        val request = CreateArticleRequest(
            title = article.title,
            description = article.description,
            content = article.content,
            moduleId = article.module?.id
        )
        val expected = ResponseEntity(
            article.toBaseResponseDto(),
            HttpStatus.CREATED
        )

        request.moduleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns module
        }
        every {
            articleRepository.save(any())
        } returns article

        // act
        val actual = articleService.create(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { articleRepository.save(any()) }
        verify(exactly = 1) { moduleRepository.findById(any()) }
    }

    @Test
    fun `should get all articles`() {
        // arrange
        val articles = listOf(article, article)
        val expected = ResponseEntity(
            articles.map { it.toBaseResponseDto() },
            HttpStatus.OK
        )

        every {
            articleRepository.findAll()
        } returns articles

        // act
        val actual = articleService.getAll()

        // assert
        assert(actual == expected)
        verify(exactly = 1) { articleRepository.findAll() }
    }

    @Test
    fun `should return updated article`() {
        // arrange
        val request = UpdateArticleRequest(
            title = article.title,
            description = article.description,
            content = article.content,
            moduleId = article.module?.id,
            id = article.id
        )
        val expected = ResponseEntity(
            article.toBaseResponseDto(),
            HttpStatus.OK
        )

        request.moduleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns module
        }
        request.id?.let {
            every {
                articleRepository.findById(it).getOrNull()
            } returns article
        }
        every {
            articleRepository.save(any())
        } returns article

        // act
        val actual = articleService.update(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { articleRepository.save(any()) }
        verify(exactly = 1) { moduleRepository.findById(any()) }
    }

    @Test
    fun `should throw exception if article not found on update`() {
        // arrange
        val request = UpdateArticleRequest(
            title = article.title,
            description = article.description,
            content = article.content,
            moduleId = article.module?.id,
            id = article.id
        )
        val expectedMessage = "Article with id \"${request.id}\" not found"

        request.moduleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns module
        }
        request.id?.let {
            every {
                articleRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            articleRepository.save(any())
        } returns article

        // act
        val message = assertThrows<NotFoundException> {
            articleService.update(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { articleRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.findById(any()) }
        verify(exactly = 0) { articleRepository.save(any()) }
    }

    @Test
    fun `should throw exception if module not found on update`() {
        // arrange
        val request = UpdateArticleRequest(
            title = article.title,
            description = article.description,
            content = article.content,
            moduleId = article.module?.id,
            id = article.id
        )
        val expectedMessage = "Module with id \"${request.moduleId}\" not found"

        request.moduleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        request.id?.let {
            every {
                articleRepository.findById(it).getOrNull()
            } returns article
        }
        every {
            articleRepository.save(any())
        } returns article

        // act
        val message = assertThrows<NotFoundException> {
            articleService.update(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { articleRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.findById(any()) }
        verify(exactly = 0) { articleRepository.save(any()) }
    }

    @Test
    fun `should delete article`() {
        // arrange
        val request = DeleteArticleRequest(
            id = article.id
        )
        val expected = ResponseEntity<HttpStatus>(HttpStatus.OK)

        request.id?.let {
            every {
                articleRepository.findById(it).getOrNull()
            } returns article
        }
        every {
            articleRepository.delete(any())
        } returns Unit

        // act
        val actual = articleService.delete(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { articleRepository.findById(any()) }
        verify(exactly = 1) { articleRepository.delete(any()) }
    }

    @Test
    fun `should throw exception if article not found on delete`() {
        // arrange
        val request = DeleteArticleRequest(
            id = article.id
        )
        val expectedMessage = "Article with id \"${request.id}\" not found"

        request.id?.let {
            every {
                articleRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            articleRepository.delete(any())
        } returns Unit

        // act
        val message = assertThrows<NotFoundException> {
            articleService.delete(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { articleRepository.findById(any()) }
        verify(exactly = 0) { articleRepository.deleteById(any()) }
    }
}
