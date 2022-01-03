package org.dbshell.shellmethods

import org.dbshell.Driver
import org.dbshell.jobqueue.JobQueue
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import java.io.IOException

@ShellComponent
class QueueMethods {
    companion object {
        private val logger = LoggerFactory.getLogger(QueueMethods::class.java)
    }
    @ShellMethod("Clean the persistent queues")
    fun cleanQueues() {
        logger.info("Cleaning job queue...")
        try {
            JobQueue.jobQueue.gc()
            logger.info("Job queue successfully cleaned.")
        } catch(iox: IOException) {
            logger.error("Error occurred cleaning the job queue: ${iox.message}")
        }
        logger.info("Closing results queue...")
        try {
            JobQueue.resultsQueue.gc()
            logger.info("Result queue successfully closed.")
        } catch(iox: IOException) {
            logger.error("Error occurred cleaning the result queue")
        }
    }
}