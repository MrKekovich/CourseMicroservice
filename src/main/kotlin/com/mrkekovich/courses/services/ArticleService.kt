package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.BaseArticleResponse
import com.mrkekovich.courses.dto.CreateArticleRequest
import com.mrkekovich.courses.dto.DeleteArticleRequest
import com.mrkekovich.courses.dto.UpdateArticleRequest
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.mappers.toBaseResponseDto
import com.mrkekovich.courses.mappers.toEntity
import com.mrkekovich.courses.repositories.ArticleRepository
import com.mrkekovich.courses.repositories.ModuleRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

private const val ARTICLE_NOT_FOUND_MESSAGE = "Article with id \"%s\" not found"

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val moduleRepository: ModuleRepository
) {
    fun create(
        dto: CreateArticleRequest
    ): BaseArticleResponse {
        val entity = articleRepository.save(
            dto.toEntity(moduleRepository)
        )

        return entity.toBaseResponseDto()
    }

    fun getAll(): List<BaseArticleResponse> =
        articleRepository.findAll().map {
            it.toBaseResponseDto()
        }

    fun update(
        dto: UpdateArticleRequest
    ): BaseArticleResponse {
        dto.id?.let { id ->
            articleRepository.findById(id).getOrNull()
        } ?: throw NotFoundException(ARTICLE_NOT_FOUND_MESSAGE.format(dto.id))

        val updatedEntity = articleRepository.save(
            dto.toEntity(moduleRepository)
        )

        return updatedEntity.toBaseResponseDto()
    }

    fun delete(
        dto: DeleteArticleRequest
    ) {
        val entity = dto.id?.let { id ->
            articleRepository.findById(id).getOrNull()
        } ?: throw NotFoundException(ARTICLE_NOT_FOUND_MESSAGE.format(dto.id))

        articleRepository.delete(entity)
    }
}
