package org.dbshell.jobqueue

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseQueueConsumer<T>(val consume: () -> T?): Runnable {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(BaseQueueConsumer::class.java)
    }
    abstract fun process(data: T)
    override fun run() {
        try {
            val payload = consume()
            payload?.let { p ->
                process(p)
            }
        } catch(ex: Exception) {
            println(ex.stackTrace)
            logger.error(ex.message)
        }
    }
}