package com.mrkekovich.courses.models

import jakarta.persistence.*

@Entity
@Table(name = "modules")
class ModuleEntity(
    @Column(name = "title")
    val title: String?,

    @Column(name = "description")
    val description: String?,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "course_id")
    val course: CourseEntity? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    val parentModule: ModuleEntity? = null,

    @OneToMany(
        mappedBy = "parent",
        cascade = [CascadeType.ALL]
    )
    val childrenModules: MutableSet<ModuleEntity> = mutableSetOf(),

    @Column(name = "order")
    val order: Int? = 0,

    @Id
    @Column(name = "id", nullable = false)
    val id: String? = null,
)
