package com.mrkekovich.coursemicroservice.services

import com.mrkekovich.coursemicroservice.dto.BaseArticleResponse
import com.mrkekovich.coursemicroservice.dto.CreateArticleRequest
import com.mrkekovich.coursemicroservice.dto.DeleteArticleRequest
import com.mrkekovich.coursemicroservice.dto.UpdateArticleRequest
import com.mrkekovich.coursemicroservice.exceptions.NotFoundException
import com.mrkekovich.coursemicroservice.mappers.toBaseResponseDto
import com.mrkekovich.coursemicroservice.mappers.toEntity
import com.mrkekovich.coursemicroservice.repositories.ArticleRepository
import com.mrkekovich.coursemicroservice.repositories.ModuleRepository
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
