package org.dbshell

import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.jobqueue.JobQueue
import org.dbshell.jobqueue.JobQueueConsumer
import org.dbshell.jobqueue.ResultsQueueConsumer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import java.io.File
import java.io.IOException
import java.lang.IllegalStateException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.LogManager
import javax.naming.Context

@SpringBootApplication
class Driver {

    final val executorService = Executors.newFixedThreadPool(4)

    init {
        if(!File("conf/jndi.properties").isFile) {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory")
            System.setProperty("org.osjava.sj.jndi.shared", "true")
            System.setProperty("org.osjava.sj.root", "src/deploy/bin/conf/jndi")
            System.setProperty("org.osjava.sj.colon.replace", "--")
            System.setProperty("org.osjava.sj.delimiter", "/")
        }
        val ctxJndi = EnvironmentProps.getCurrentContextAndJndi()
        EnvironmentVars.setCurrentContextAndJndi(ctxJndi.context, ctxJndi.jndi)
        val catalog = EnvironmentProps.getCurrentCatalog()
        EnvironmentVars.setCurrentCatalog(catalog)
        val schema = EnvironmentProps.getCurrentSchema()
        EnvironmentVars.setCurrentSchema(schema)

        //Start listening thread for job queue
        executorService.execute(JobQueueConsumer())
        executorService.execute(ResultsQueueConsumer())
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
                logger.info("Job queue successfully closed.")
            } catch(iox: IOException) {
                logger.error("Error occurred closing the job queue: ${iox.message}")
            }
        }
    }
}

fun main(args: Array<String>) {
    LogManager.getLogManager().reset()
    Driver().start(args)
}