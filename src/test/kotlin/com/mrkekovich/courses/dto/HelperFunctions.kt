package com.mrkekovich.courses.dto

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory

private val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
val validator: Validator = factory.validator

/**
 * Returns list of strings with length from min to max (inclusive).
 *
 * @param range range of lengths to get.
 *
 * @throws IllegalArgumentException if min < 0 or max < 0 or min > max
 *
 * @return list of strings of length from min to max (inclusive).
 */
fun stringRange(
    range: IntRange,
    char: Char
): List<String> {
    when {
        range.first < 0 -> throw IllegalArgumentException("range.first must be >= 0")
        range.last < 0 -> throw IllegalArgumentException("range.last must be >= 0")
    }
    return (range).map {
        char.toString().repeat(it)
    }
}

fun validateLength(
    validLengthRange: IntRange,
    testRange: IntRange,
    createDto: (field: String) -> Any,
) {
    val stringRange = stringRange(testRange, 'a')
    for (field in stringRange) {
        val dto = createDto(field)

        val violations =
            validator.validate(dto)

        if (field.length in validLengthRange) {
            assert(violations.isEmpty())
        } else {
            assert(violations.isNotEmpty())
        }
    }
}

fun validateNotNull(
    createDto: (field: String?) -> Any,
) {
    val invalidDto = createDto(null)

    val violations =
        validator.validate(invalidDto)

    assert(violations.isNotEmpty())
}

fun validateNotBlank(
    createDto: (field: String?) -> Any,
) {
    val invalidBlankFieldDto = createDto("")
    val invalidNullFieldDto = createDto(null)

    val violationsBlankField =
        validator.validate(invalidBlankFieldDto)

    val violationsNullField =
        validator.validate(invalidNullFieldDto)

    assert(violationsBlankField.isNotEmpty())
    assert(violationsNullField.isNotEmpty())
}
