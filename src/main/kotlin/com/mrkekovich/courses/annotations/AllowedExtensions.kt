package com.mrkekovich.courses.annotations

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile
import java.io.File
import kotlin.reflect.KClass

/**
 * Allowed extension validation.
 * Validates [MultipartFile] extension.
 * If extension is not in [allowedExtensions] or is null returns false.
 * @property allowedExtensions
 * @property message
 * @property groups
 * @property payload
 * @constructor Create empty Allowed extensions
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ExtensionValidator::class])
annotation class AllowedExtensions(
    val allowedExtensions: Array<String>,
    val message: String = "Invalid file extension",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Any>> = []
)

private class ExtensionValidator : ConstraintValidator<AllowedExtensions, MultipartFile?> {
    lateinit var allowedExtensions: Array<String>

    override fun initialize(constraintAnnotation: AllowedExtensions) {
        allowedExtensions = constraintAnnotation.allowedExtensions
    }

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext?): Boolean {
        val extension = value?.originalFilename?.let { File(it).extension }
            ?: return false
        return allowedExtensions.contains(extension)

    }
}
