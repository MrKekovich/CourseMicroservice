package com.mrkekovich.courses.mappers

import com.mrkekovich.courses.dto.ArticleDto
import com.mrkekovich.courses.dto.BaseArticleResponse
import com.mrkekovich.courses.dto.CreateArticleRequest
import com.mrkekovich.courses.dto.UpdateArticleRequest
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.models.ArticleEntity
import com.mrkekovich.courses.repositories.ModuleRepository
import kotlin.jvm.optionals.getOrNull

/**
 * Converts [ArticleEntity] to [BaseArticleResponse].
 *
 * @return [BaseArticleResponse] mapped from [ArticleEntity].
 */
fun ArticleEntity.toBaseResponseDto(): BaseArticleResponse {
    return BaseArticleResponse(
        title = title,
        content = content,
        description = description,
        moduleId = module?.id,
        id = id,
    )
}

/**
 * Create request to entity
 *
 * Transforms create request DTO to entity
 * @param moduleRepository [ModuleRepository]
 * @return [ArticleEntity] mapped from [CreateArticleRequest]
 */
fun CreateArticleRequest.toEntity(
    moduleRepository: ModuleRepository
): ArticleEntity = dtoToEntity(
    this,
    moduleRepository
)

/**
 * Update request to entity
 *
 * Transforms update request DTO to entity
 * @param moduleRepository [ModuleRepository]
 * @return [ArticleEntity] mapped from [UpdateArticleRequest]
 */
fun UpdateArticleRequest.toEntity(
    moduleRepository: ModuleRepository
): ArticleEntity = dtoToEntity(
    this,
    moduleRepository,
)

/**
 * Dto to entity
 *
 * Converts dto to an entity.
 * By default, parent DTOs have all fields we need set to null.
 * So be careful when creating an entity from DTO that does not
 * override all fields - they will be set to null.
 *
 * @param dto The [ArticleDto] to convert to entity.
 * @param moduleRepository [ModuleRepository] Used to map module from id.
 * @return [ArticleEntity] mapped from [dto].
 */
private fun dtoToEntity(
    dto: ArticleDto,
    moduleRepository: ModuleRepository,
): ArticleEntity = ArticleEntity(
    title = dto.title,
    content = dto.content,
    description = dto.description,
    module = dto.moduleId?.let {
        moduleRepository.findById(it).getOrNull()
    } ?: throw NotFoundException("Module with id \"${dto.moduleId}\" not found"),
    id = dto.id
)
