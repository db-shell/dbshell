package org.dbshell

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication

@SpringBootApplication
class Driver {

    companion object {
        private val logger = LoggerFactory.getLogger(Driver::class.java)
    }

    fun start(args: Array<String>) {
        val app = SpringApplication(Driver::class.java)
        app.run(*args)
    }
}

fun main(args: Array<String>) {
    Driver().start(args)
}