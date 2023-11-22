package com.mrkekovich.courses

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// @EnableDiscoveryClient
@SpringBootApplication
class CoursesApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<CoursesApplication>(*args)
}
