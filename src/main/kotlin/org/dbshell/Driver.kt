package org.dbshell

import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import java.io.File
import java.lang.IllegalStateException
import java.util.logging.LogManager
import javax.naming.Context

@SpringBootApplication
class Driver {

    init {
        if(!File("conf/jndi.properties").isFile) {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory")
            System.setProperty("org.osjava.sj.jndi.shared", "true")
            System.setProperty("org.osjava.sj.root", "src/deploy/bin/conf/jndi")
            System.setProperty("org.osjava.sj.colon.replace", "--")
            System.setProperty("org.osjava.sj.delimiter", "/")
        }
        val (currentContext, currentJndi) = EnvironmentProps.getCurrentContextAndJndi()
        EnvironmentVars.setCurrentContextAndJndi(currentContext, currentJndi)
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
        }
    }
}

fun main(args: Array<String>) {
    LogManager.getLogManager().reset()
    Driver().start(args)
}