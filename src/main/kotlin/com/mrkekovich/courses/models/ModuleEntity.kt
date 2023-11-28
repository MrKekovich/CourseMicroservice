package com.mrkekovich.courses.models

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

/**
 * Module entity.
 *
 * @property title Title of a module.
 * @property description Description of a module.
 * @property position Position of a module.
 * @property course Course the module belongs to.
 * @property parentModule Parent module.
 * @property id ID of a module.
 * @property childrenModules Children modules of a module.
 * @property articles Articles of a module.
 */
@Entity
@Table(name = "modules")
class ModuleEntity(
    @Column(name = "title")
    val title: String?,

    @Column(name = "description")
    val description: String?,

    @Column(name = "position")
    val position: Int? = 0,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "course_id")
    val course: CourseEntity? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    val parentModule: ModuleEntity? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
) {
    @OneToMany(
        mappedBy = "parentModule",
        cascade = [CascadeType.ALL]
    )
    var childrenModules: MutableSet<ModuleEntity> = mutableSetOf()

    @OneToMany(
        mappedBy = "module",
        cascade = [CascadeType.ALL]
    )
    var articles: MutableSet<ArticleEntity> = mutableSetOf()
}
