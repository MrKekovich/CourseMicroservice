package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.BaseArticleResponse
import com.mrkekovich.courses.dto.CreateArticleRequest
import com.mrkekovich.courses.dto.DeleteArticleRequest
import com.mrkekovich.courses.dto.GetAllArticlesRequest
import com.mrkekovich.courses.dto.UpdateArticleRequest
import com.mrkekovich.courses.services.ArticleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    @Operation(summary = "Get all articles")
    @ApiResponse(
        responseCode = "200",
        description = "Get all articles",
        content = [
            Content(
                schema = Schema(implementation = BaseArticleResponse::class)
            )
        ]
    )
    @GetMapping
    fun getArticles(
        @Validated
        @RequestBody
        dto: GetAllArticlesRequest
    ): ResponseEntity<List<BaseArticleResponse>> =
        articleService.getAll(dto)

    @Operation(summary = "Create new article")
    @ApiResponse(
        responseCode = "200",
        description = "Create new article",
        content = [
            Content(
                schema = Schema(implementation = BaseArticleResponse::class)
            )
        ]
    )
    @PostMapping
    fun createArticle(
        @Validated
        @RequestBody
        dto: CreateArticleRequest
    ): ResponseEntity<BaseArticleResponse> =
        articleService.create(dto)

    @Operation(summary = "Update article")
    @ApiResponse(
        responseCode = "200",
        description = "Update article",
        content = [
            Content(
                schema = Schema(implementation = BaseArticleResponse::class)
            )
        ]
    )
    @PatchMapping
    fun updateArticle(
        @Validated
        @RequestBody
        dto: UpdateArticleRequest
    ): ResponseEntity<BaseArticleResponse> =
        articleService.update(dto)

    @Operation(summary = "Delete article")
    @ApiResponse(
        responseCode = "200",
        description = "Delete article",
        content = [
            Content(
                schema = Schema(implementation = BaseArticleResponse::class)
            )
        ]
    )
    @DeleteMapping
    fun deleteArticle(
        @Validated
        @RequestBody
        dto: DeleteArticleRequest
    ): ResponseEntity<HttpStatus> =
        articleService.delete(dto)
}
