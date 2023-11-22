package com.mrkekovich.courses.dto

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory

abstract class BaseTestDto {
    private val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    protected val validator: Validator = factory.validator
}