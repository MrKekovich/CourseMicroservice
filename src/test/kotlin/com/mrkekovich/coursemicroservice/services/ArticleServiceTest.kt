package com.mrkekovich.coursemicroservice.services

import com.mrkekovich.coursemicroservice.dto.CreateArticleRequest
import com.mrkekovich.coursemicroservice.dto.DeleteArticleRequest
import com.mrkekovich.coursemicroservice.dto.UpdateArticleRequest
import com.mrkekovich.coursemicroservice.exceptions.NotFoundException
import com.mrkekovich.coursemicroservice.mappers.toBaseResponseDto
import com.mrkekovich.coursemicroservice.models.ArticleEntity
import com.mrkekovich.coursemicroservice.models.CourseEntity
import com.mrkekovich.coursemicroservice.models.ModuleEntity
import com.mrkekovich.coursemicroservice.repositories.ArticleRepository
import com.mrkekovich.coursemicroservice.repositories.ModuleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        val expected = article.toBaseResponseDto()

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
        val expected = articles.map { it.toBaseResponseDto() }

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
        val expected = article.toBaseResponseDto()

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
        val expected = Unit

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
