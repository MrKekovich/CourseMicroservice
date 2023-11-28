package com.mrkekovich.courses.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfiguration {
    @Bean
    fun openApi(): OpenAPI = OpenAPI().apply {
        info = Info().apply {
            title = "Netrunner Courses API"
            version = "0.1.0a0"
            description = "API for managing courses"
            contact = Contact().apply {
                name = "MrKekovich"
                email = "mrkekovich.official@gmail.com"
            }
        }
    }
}
