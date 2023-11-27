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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val moduleRepository: ModuleRepository
) {
    fun create(dto: CreateArticleRequest): ResponseEntity<BaseArticleResponse> {
        val entity = articleRepository.save(
            dto.toEntity(moduleRepository)
        )

        return ResponseEntity(
            entity.toBaseResponseDto(),
            HttpStatus.CREATED
        )
    }

    @Suppress("UnusedParameter")
    fun getAll(
//        dto: GetAllArticlesRequest TODO: add pagination
    ): ResponseEntity<List<BaseArticleResponse>> {
        val response = articleRepository.findAll().map {
            it.toBaseResponseDto()
        }

        return ResponseEntity(
            response,
            HttpStatus.OK
        )
    }

    fun update(dto: UpdateArticleRequest): ResponseEntity<BaseArticleResponse> {
        dto.id?.let {
            articleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Article with id \"${dto.id}\" not found")

        val updatedEntity = articleRepository.save(
            dto.toEntity(moduleRepository)
        )

        return ResponseEntity(
            updatedEntity.toBaseResponseDto(),
            HttpStatus.OK
        )
    }

    fun delete(dto: DeleteArticleRequest): ResponseEntity<HttpStatus> {
        val entity = dto.id?.let {
            articleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Article with id \"${dto.id}\" not found")

        articleRepository.delete(entity)

        return ResponseEntity(
            HttpStatus.OK
        )
    }
}
