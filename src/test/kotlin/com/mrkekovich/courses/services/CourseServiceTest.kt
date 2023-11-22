package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CourseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.repositories.CourseRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import kotlin.jvm.optionals.getOrNull

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseServiceTest {
    private var courseEntity1: CourseEntity? = null
    private var courseEntity2: CourseEntity? = null

    @Autowired
    lateinit var courseService: CourseService

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseEntity1 = courseRepository.save(
            CourseEntity(
                title = "title2",
                description = "description2",
            )
        )
        courseEntity2 = courseRepository.save(
            CourseEntity(
                title = "title1",
                description = "description1",
            )
        )
    }

    fun assertEqualResponse(
        actual: CourseDto.Response.Base?,
        expected: CourseEntity?,
    ) {
        assert(expected?.id == actual?.id)
        assert(expected?.title == actual?.title)
        assert(expected?.description == actual?.description)
    }

    @Test
    fun `create new course`() {
        // <editor-fold desc="Arrange">
        val dto = CourseDto.Request.Create(
            title = "title3",
            description = "description3",
        )
        // </editor-fold>

        // <editor-fold desc="Act">
        val createResponse = courseService.create(dto)
        val actualEntity = createResponse.body?.id?.let {
            courseRepository.findById(it).getOrNull()
        } ?: throw AssertionError("Could not find new course")
        val entityCount = courseRepository.findAll().count()
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(actualEntity.title == "title3")
        assert(actualEntity.description == "description3")
        assert(createResponse.statusCode.is2xxSuccessful)
        assert(entityCount == 3)
        // </editor-fold>
    }

    @Test
    fun `get all courses`() {
        // <editor-fold desc="Arrange">
        val dto = CourseDto.Request.GetAll()
        // </editor-fold>

        // <editor-fold desc="Act">
        val courseResponses = courseService.getAll(dto)
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(courseResponses.body?.size == 2)
        assertEqualResponse(courseResponses.body?.get(0), courseEntity1)
        assertEqualResponse(courseResponses.body?.get(1), courseEntity2)
        // </editor-fold>
    }

    @Test
    fun `update existing course`() {
        // <editor-fold desc="Arrange">
        val dto = courseEntity1?.id?.let {
            CourseDto.Request.Update(
                title = "new",
                description = "new",
                id = it,
            )
        } ?: throw AssertionError("Either courseEntity1 or it's id is null")
        // </editor-fold>

        // <editor-fold desc="Act">
        val updateResponse = courseService.update(dto)
        val actualEntity = courseEntity1?.id?.let {
            courseRepository.findById(it).getOrNull()
        } ?: throw AssertionError("Could not find courseEntity1")
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(courseEntity1?.id == actualEntity.id)
        assert(updateResponse.body?.id == actualEntity.id)

        assert(updateResponse.body?.title == "new")
        assert(updateResponse.body?.description == "new")

        assert(actualEntity.title == "new")
        assert(actualEntity.description == "new")
        // </editor-fold>
    }

    @Test
    fun `delete existing course`() {
        // <editor-fold desc="Arrange">
        val dto = courseEntity1?.id?.let {
            CourseDto.Request.Delete(
                id = it
            )
        } ?: throw AssertionError("Either courseEntity1 or it's id is null")
        // </editor-fold>

        // <editor-fold desc="Act">
        val deleteResponse = courseService.delete(dto)
        val actualEntity = courseEntity1?.id?.let {
            courseRepository.findById(it).getOrNull()
        }
        // </editor-fold>

        // <editor-fold desc="Assert">
        assert(actualEntity == null)
        assert(deleteResponse.statusCode.is2xxSuccessful)
        // </editor-fold>
    }

    @AfterEach
    fun tearDown() {
        courseRepository.deleteAll()
    }
}
