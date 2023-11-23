package com.mrkekovich.courses.controllers

import com.mrkekovich.courses.dto.ArticleDto
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
                schema = Schema(implementation = ArticleDto.Response.Base::class)
            )
        ]
    )
    @GetMapping
    fun getArticles(
        @Validated
        @RequestBody
        dto: ArticleDto.Request.GetAll
    ): ResponseEntity<List<ArticleDto.Response.Base>> =
        articleService.getArticles(dto)

    @Operation(summary = "Create new article")
    @ApiResponse(
        responseCode = "200",
        description = "Create new article",
        content = [
            Content(
                schema = Schema(implementation = ArticleDto.Response.Base::class)
            )
        ]
    )
    @PostMapping
    fun createArticle(
        @Validated
        @RequestBody
        dto: ArticleDto.Request.Create
    ): ResponseEntity<ArticleDto.Response.Base> =
        articleService.createArticle(dto)

    @Operation(summary = "Update article")
    @ApiResponse(
        responseCode = "200",
        description = "Update article",
        content = [
            Content(
                schema = Schema(implementation = ArticleDto.Response.Base::class)
            )
        ]
    )
    @PatchMapping
    fun updateArticle(
        @Validated
        @RequestBody
        dto: ArticleDto.Request.Update
    ): ResponseEntity<ArticleDto.Response.Base> =
        articleService.updateArticle(dto)

    @Operation(summary = "Delete article")
    @ApiResponse(
        responseCode = "200",
        description = "Delete article",
        content = [
            Content(
                schema = Schema(implementation = ArticleDto.Response.Base::class)
            )
        ]
    )
    @DeleteMapping
    fun deleteArticle(
        @Validated
        @RequestBody
        dto: ArticleDto.Request.Delete
    ): ResponseEntity<HttpStatus> =
        articleService.deleteArticle(dto)
}
