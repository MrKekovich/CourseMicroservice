package com.mrkekovich.coursemicroservice.controllers

import com.mrkekovich.coursemicroservice.dto.BaseArticleResponse
import com.mrkekovich.coursemicroservice.dto.CreateArticleRequest
import com.mrkekovich.coursemicroservice.dto.DeleteArticleRequest
import com.mrkekovich.coursemicroservice.dto.UpdateArticleRequest
import com.mrkekovich.coursemicroservice.services.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/articles")
@Tag(name = "Article controller")
class ArticleController(
    private val articleService: ArticleService
) {
    @ApiResponse(
        responseCode = "200",
        description = "Get articles",
        content = [Content(schema = Schema(implementation = BaseArticleResponse::class))]
    )
    @Operation(summary = "Get articles")
    @GetMapping
    fun getArticles(): List<BaseArticleResponse> =
        articleService.getAll()

    @ApiResponse(
        responseCode = "200",
        description = "Create new article",
        content = [Content(schema = Schema(implementation = BaseArticleResponse::class))]
    )
    @Operation(summary = "Create new article")
    @PostMapping
    fun createArticle(
        @Validated
        @RequestBody
        dto: CreateArticleRequest
    ): BaseArticleResponse =
        articleService.create(dto)

    @ApiResponse(
        responseCode = "200",
        description = "Update article",
        content = [Content(schema = Schema(implementation = BaseArticleResponse::class))]
    )
    @Operation(summary = "Update article")
    @PatchMapping
    fun updateArticle(
        @Validated
        @RequestBody
        dto: UpdateArticleRequest
    ): BaseArticleResponse =
        articleService.update(dto)

    @ApiResponse(
        responseCode = "200",
        description = "Delete article",
        content = [Content(schema = Schema(implementation = BaseArticleResponse::class))]
    )
    @Operation(summary = "Delete article")
    @DeleteMapping
    fun deleteArticle(
        @Validated
        @RequestBody
        dto: DeleteArticleRequest
    ): Unit =
        articleService.delete(dto)
}
