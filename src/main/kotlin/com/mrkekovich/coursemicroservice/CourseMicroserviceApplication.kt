package com.mrkekovich.coursemicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// @EnableDiscoveryClient
@SpringBootApplication
class CourseMicroserviceApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<CourseMicroserviceApplication>(*args)
}
