package com.mrkekovich.courses.annotations

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile
import java.io.File
import kotlin.reflect.KClass

/**
 * Validates [MultipartFile] extension.
 * If extension is not in [allowedExtensions] or is null returns false.
 * @property allowedExtensions Array of allowed extensions.
 * @property message Error message.
 * @property groups Validation groups.
 * @property payload Validation payload.
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ExtensionValidator::class])
annotation class AllowedExtensions(
    val allowedExtensions: Array<String>,
    val message: String = "Invalid file extension",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Any>> = [],
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
