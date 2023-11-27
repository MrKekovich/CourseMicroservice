package com.mrkekovich.courses.services

import com.mrkekovich.courses.dto.CreateModuleRequest
import com.mrkekovich.courses.dto.DeleteModuleRequest
import com.mrkekovich.courses.dto.UpdateModuleRequest
import com.mrkekovich.courses.exceptions.NotFoundException
import com.mrkekovich.courses.mappers.toBaseResponseDto
import com.mrkekovich.courses.models.CourseEntity
import com.mrkekovich.courses.models.ModuleEntity
import com.mrkekovich.courses.repositories.CourseRepository
import com.mrkekovich.courses.repositories.ModuleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.jvm.optionals.getOrNull

internal class ModuleServiceTest {
    private val moduleRepository: ModuleRepository = mockk()
    private val courseRepository: CourseRepository = mockk()

    private val moduleService = ModuleService(moduleRepository, courseRepository)

    private val course = CourseEntity(
        title = "title",
        description = "description",
        id = "id"
    )
    private val module = ModuleEntity(
        title = "title",
        description = "description",
        course = course,
        id = "id",
    )
    private val nestedModule = ModuleEntity(
        title = "title",
        description = "description",
        course = course,
        parentModule = module,
        id = "nested_id",
    )

    @Test
    fun `should return correct module on create`() {
        // arrange
        val request = CreateModuleRequest(
            title = module.title,
            description = module.description,
            position = module.position,
            parentModuleId = module.parentModule?.id,
            courseId = module.course?.id,
        )
        val expected = ResponseEntity(
            module.toBaseResponseDto(),
            HttpStatus.CREATED
        )

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns course
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            moduleRepository.save(any())
        } returns module

        // act
        val actual = moduleService.create(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.save(any()) }
    }

    @Test
    fun `should return correct nested module on create`() {
        // arrange
        val request = CreateModuleRequest(
            title = nestedModule.title,
            description = nestedModule.description,
            position = nestedModule.position,
            parentModuleId = nestedModule.parentModule?.id,
            courseId = nestedModule.course?.id,
        )
        val expected = ResponseEntity(
            nestedModule.toBaseResponseDto(),
            HttpStatus.CREATED
        )

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns course
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns module
        }
        every {
            moduleRepository.save(any())
        } returns nestedModule

        // act
        val actual = moduleService.create(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { moduleRepository.findById(any()) }
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.save(any()) }
    }

    @Test
    fun `should throw exception when course not found on create`() {
        // arrange
        val request = CreateModuleRequest(
            title = module.title,
            description = module.description,
            parentModuleId = module.parentModule?.id,
            courseId = module.course?.id,
            position = module.position,
        )
        val expectedMessage = "Course with id ${request.courseId} not found"

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            moduleRepository.save(any())
        } returns module

        // act
        val message = assertThrows<NotFoundException> {
            moduleService.create(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.save(any()) }
    }

    @Test
    fun `should throw exception when parent module not found on create`() {
        // arrange
        val request = CreateModuleRequest(
            title = nestedModule.title,
            description = nestedModule.description,
            parentModuleId = nestedModule.parentModule?.id,
            courseId = nestedModule.course?.id,
            position = nestedModule.position,
        )
        val expectedMessage = "Module with id ${request.parentModuleId} not found"

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns course
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            moduleRepository.save(any())
        } returns nestedModule

        // act
        val message = assertThrows<NotFoundException> {
            moduleService.create(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.save(any()) }
    }

    @Test
    fun `should throw exception course not found when both parent and course not found on create`() {
        // arrange
        val request = CreateModuleRequest(
            title = nestedModule.title,
            description = nestedModule.description,
            parentModuleId = nestedModule.parentModule?.id,
            courseId = nestedModule.course?.id,
            position = nestedModule.position,
        )
        val expectedMessage = "Course with id ${request.courseId} not found"

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns null
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            moduleRepository.save(any())
        } returns nestedModule

        // act
        val message = assertThrows<NotFoundException> {
            moduleService.create(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.save(any()) }
    }

    @Test
    fun `should get all modules on get all`() {
        // arrange
        val records = listOf(
            module,
            nestedModule
        )
        val expected = ResponseEntity(
            records.map { it.toBaseResponseDto() },
            HttpStatus.OK
        )

        every {
            moduleRepository.findAll()
        } returns records

        // act
        val actual = moduleService.getAll()

        // assert
        assert(actual == expected)
        verify(exactly = 1) { moduleRepository.findAll() }
    }

    @Test
    fun `should return correct module on update`() {
        // arrange
        val request = UpdateModuleRequest(
            title = module.title,
            description = module.description,
            position = module.position,
            courseId = module.course?.id,
            parentModuleId = module.parentModule?.id,
            id = module.id,
        )
        val expected = ResponseEntity(
            module.toBaseResponseDto(),
            HttpStatus.OK
        )

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns course
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        request.id?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns module
        }
        every {
            moduleRepository.save(any())
        } returns module

        // act
        val actual = moduleService.update(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.save(any()) }
    }

    @Test
    fun `should return correct nested module on update`() {
        // arrange
        val request = UpdateModuleRequest(
            title = nestedModule.title,
            description = nestedModule.description,
            position = nestedModule.position,
            courseId = nestedModule.course?.id,
            parentModuleId = nestedModule.parentModule?.id,
            id = nestedModule.id,
        )
        val expected = ResponseEntity(
            nestedModule.toBaseResponseDto(),
            HttpStatus.OK
        )

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns course
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns module
        }
        request.id?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns nestedModule
        }
        every {
            moduleRepository.save(any())
        } returns nestedModule

        // act
        val actual = moduleService.update(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 2) { moduleRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.save(any()) }
    }

    @Test
    fun `should throw exception when course not found on update`() {
        // arrange
        val request = UpdateModuleRequest(
            title = module.title,
            description = module.description,
            position = module.position,
            courseId = module.course?.id,
            parentModuleId = module.parentModule?.id,
            id = module.id,
        )
        val expectedMessage = "Course with id ${request.courseId} not found"

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns null
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        request.id?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns module
        }
        every {
            moduleRepository.save(any())
        } returns module

        // act
        val message = assertThrows<NotFoundException> {
            moduleService.update(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.save(any()) }
    }

    @Test
    fun `should throw exception when parent module not found on update`() {
        // arrange
        val request = UpdateModuleRequest(
            title = nestedModule.title,
            description = nestedModule.description,
            position = nestedModule.position,
            courseId = nestedModule.course?.id,
            parentModuleId = nestedModule.parentModule?.id,
            id = nestedModule.id,
        )
        val expectedMessage = "Module with id ${request.parentModuleId} not found"

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns course
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        request.id?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns nestedModule
        }
        every {
            moduleRepository.save(any())
        } returns nestedModule

        // act
        val message = assertThrows<NotFoundException> {
            moduleService.update(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 2) { moduleRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.save(any()) }
    }

    @Test
    fun `should throw course not found when both parent and course not found on update`() {
        // arrange
        val request = UpdateModuleRequest(
            title = nestedModule.title,
            description = nestedModule.description,
            position = nestedModule.position,
            courseId = nestedModule.course?.id,
            parentModuleId = nestedModule.parentModule?.id,
            id = nestedModule.id,
        )
        val expectedMessage = "Course with id ${request.courseId} not found"

        request.courseId?.let {
            every {
                courseRepository.findById(it).getOrNull()
            } returns null
        }
        request.parentModuleId?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        request.id?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns nestedModule
        }
        every {
            moduleRepository.save(any())
        } returns nestedModule

        // act
        val message = assertThrows<NotFoundException> {
            moduleService.update(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { courseRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.save(any()) }
    }

    @Test
    fun `should delete module`() {
        // arrange
        val request = DeleteModuleRequest(module.id)
        val expected = ResponseEntity<HttpStatus>(HttpStatus.OK)

        request.id?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns module
        }
        every {
            moduleRepository.delete(any())
        } returns Unit

        // act
        val actual = moduleService.delete(request)

        // assert
        assert(actual == expected)
        verify(exactly = 1) { moduleRepository.findById(any()) }
        verify(exactly = 1) { moduleRepository.delete(any()) }
    }

    @Test
    fun `should throw exception when module not found on delete`() {
        // arrange
        val request = DeleteModuleRequest(module.id)
        val expectedMessage = "Module with id ${request.id} not found"

        request.id?.let {
            every {
                moduleRepository.findById(it).getOrNull()
            } returns null
        }
        every {
            moduleRepository.delete(any())
        } returns Unit

        // act
        val message = assertThrows<NotFoundException> {
            moduleService.delete(request)
        }.message

        // assert
        assert(message == expectedMessage)
        verify(exactly = 1) { moduleRepository.findById(any()) }
        verify(exactly = 0) { moduleRepository.delete(any()) }
    }
}
