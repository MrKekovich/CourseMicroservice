package com.mrkekovich.coursemicroservice.dto

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory

val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
val validator: Validator = factory.validator

fun validateLength(
    minLength: Int? = null,
    maxLength: Int,
    createDto: (field: String) -> Any,
) {
    // arrange
    val validDtoMaxLength = createDto("1".repeat(maxLength))

    val validDtoMinLength = minLength?.let {
        assert(minLength > 0) { "minLength must be greater than 0" }
        createDto("".repeat(maxLength - 1))
    }

    val invalidOverMaxLengthDto = createDto("1".repeat(maxLength + 1))

    val invalidUnderMinLengthDto = minLength?.let {
        assert(minLength > 0) { "minLength must be greater than 0" }
        createDto("".repeat(maxLength - 1))
    }

    // act
    val violationsValidMaxLengthDto =
        validator.validate(validDtoMaxLength)

    val violationsValidMinLengthDto =
        validDtoMinLength?.let {
            validator.validate(validDtoMinLength)
        }

    val violationsOverMaxLength =
        validator.validate(invalidOverMaxLengthDto)

    val violationsUnderMinLength = invalidUnderMinLengthDto?.let {
        validator.validate(invalidUnderMinLengthDto)
    }

    // assert
    assert(violationsValidMaxLengthDto.isEmpty())
    violationsValidMinLengthDto?.let {
        assert(it.isEmpty())
    }

    assert(violationsOverMaxLength.isNotEmpty())
    violationsUnderMinLength?.let {
        assert(it.isNotEmpty())
    }
}

fun <T : Any> validateNotNull(
    expectedViolationsCount: Int,
    createDto: (field: T?) -> Any,
) {
    // arrange
    val invalidDto = createDto(null)

    // act
    val violations =
        validator.validate(invalidDto)

    // assert
    assert(violations.size == expectedViolationsCount)
}

fun validateNotBlankString(
    expectedViolationsCount: Int,
    createDto: (field: String?) -> Any,
) {
    // arrange
    val invalidBlankFieldDto = createDto("")
    val invalidWhiteSpaceValueDto = createDto(" ")

    // act
    val violationsBlankField =
        validator.validate(invalidBlankFieldDto)

    val violationsWhiteSpaceValue =
        validator.validate(invalidWhiteSpaceValueDto)

    // assert
    assert(violationsBlankField.size == expectedViolationsCount)
    assert(violationsWhiteSpaceValue.size == expectedViolationsCount)
}
