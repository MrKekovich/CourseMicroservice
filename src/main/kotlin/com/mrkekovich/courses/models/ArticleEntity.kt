package com.mrkekovich.courses.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "article_entity")
class ArticleEntity(
    @Column(name = "title")
    val title: String?,

    @Column(name = "description")
    val description: String?,

    @Column(name = "content")
    val content: String?,

    @ManyToOne
    @JoinColumn(name = "module_id")
    val module: ModuleEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null
)
