package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.ArticleDto
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.models.ArticleEntity
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
    private fun ArticleDto.Request.Create.toEntity(): ArticleEntity {
        return ArticleEntity(
            title = title,
            content = content,
            description = description,
            module = moduleId?.let {
                moduleRepository.findById(it).getOrNull()
            } ?: throw NotFoundException("Module with id $moduleId not found")
        )
    }

    private fun ArticleDto.Request.Update.toEntity(): ArticleEntity {
        return ArticleEntity(
            id = id,
            title = title,
            content = content,
            description = description,
            module = moduleId?.let {
                moduleRepository.findById(it).getOrNull()
            } ?: throw NotFoundException("Module with id $moduleId not found")
        )
    }

    fun createArticle(dto: ArticleDto.Request.Create): ResponseEntity<ArticleDto.Response.Base> {
        val entity = articleRepository.save(dto.toEntity())
        return ResponseEntity(
            entity.toBaseResponseDto(),
            HttpStatus.CREATED
        )
    }

    @Suppress("UnusedParameter")
    fun getArticles(
        dto: ArticleDto.Request.GetAll
    ): ResponseEntity<List<ArticleDto.Response.Base>> {
        val response = articleRepository.findAll().map { it.toBaseResponseDto() }
        return ResponseEntity(
            response,
            HttpStatus.OK
        )
    }

    fun updateArticle(dto: ArticleDto.Request.Update): ResponseEntity<ArticleDto.Response.Base> {
        dto.id?.let {
            articleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Article with id ${dto.id} not found")

        val updatedEntity = articleRepository.save(dto.toEntity())
        return ResponseEntity(
            updatedEntity.toBaseResponseDto(),
            HttpStatus.OK
        )
    }

    fun deleteArticle(dto: ArticleDto.Request.Delete): ResponseEntity<HttpStatus> {
        val entity = dto.id?.let {
            articleRepository.findById(it).getOrNull()
        } ?: throw NotFoundException("Article with id ${dto.id} not found")
        articleRepository.delete(entity)
        return ResponseEntity(
            HttpStatus.OK
        )
    }
}

private fun ArticleEntity.toBaseResponseDto(): ArticleDto.Response.Base {
    return ArticleDto.Response.Base(
        title = title,
        content = content,
        description = description,
        moduleId = module.id,
        id = id,
    )
}
