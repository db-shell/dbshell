package org.dbshell

import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.jobqueue.JobQueue
import org.dbshell.jobqueue.JobQueueConsumer
import org.dbshell.jobqueue.ResultsQueueConsumer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import java.io.File
import java.io.IOException
import java.lang.IllegalStateException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.logging.LogManager
import javax.naming.Context
import kotlin.system.exitProcess

@SpringBootApplication(exclude = [R2dbcAutoConfiguration::class])
class Driver {

    private final val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(4)

    init {
        if(!File("conf/jndi.properties").isFile) {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory")
            System.setProperty("org.osjava.sj.jndi.shared", "true")
            System.setProperty("org.osjava.sj.root", "src/deploy/bin/conf/jndi")
            System.setProperty("org.osjava.sj.colon.replace", "--")
            System.setProperty("org.osjava.sj.delimiter", "/")
        }
        val ctxJndi = EnvironmentProps.getCurrentContextAndJndi()
        EnvironmentVars.currentContextAndJndi(ctxJndi.context, ctxJndi.jndi)
        val catalog = EnvironmentProps.getCurrentCatalog()
        EnvironmentVars.currentCatalog = catalog
        val schema = EnvironmentProps.getCurrentSchema()
        EnvironmentVars.currentSchema = schema

        //Start listening thread for job queue
        executorService.scheduleWithFixedDelay(JobQueueConsumer(), 1,  1, TimeUnit.SECONDS)
        executorService.scheduleWithFixedDelay(ResultsQueueConsumer(), 1,  1, TimeUnit.SECONDS)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Driver::class.java)
    }

    fun start(args: Array<String>) {
        try {
            val app = SpringApplication(Driver::class.java)
            app.run(*args)
        } catch(iex: IllegalStateException) {
            logger.error("Error occurred while running Spring Shell: ${iex.message}")
        } finally {
            logger.info("Shutting down executor service...")
            executorService.shutdown()
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow()
                }
            } catch(iex:InterruptedException) {
                executorService.shutdownNow()
                Thread.currentThread().interrupt()
                logger.error(iex.message)
                throw iex
            }
            logger.info("Closing job queue...")
            try {
                JobQueue.jobQueue.close()
                logger.info("Job queue successfully closed.")
            } catch(iox: IOException) {
                logger.error("Error occurred closing the job queue: ${iox.message}")
            }
            logger.info("Closing results queue...")
            try {
                JobQueue.resultsQueue.close()
                logger.info("Result queue successfully closed.")
            } catch(iox: IOException) {
                logger.error("Error occurred closing the result queue: ${iox.message}")
            }
            logger.info("Stopping application...")
            try {
                exitProcess(0)
            } catch(rex: RuntimeException) {
                logger.info("Error occurred closing the application: ${rex.message}")
            }
        }
    }
}

fun main(args: Array<String>) {
    LogManager.getLogManager().reset()
    Driver().start(args)
}