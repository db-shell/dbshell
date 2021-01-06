package org.dbshell.commands.connections

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.*

@ShellComponent
class JndiManager {

    companion object {
        private val logger = LoggerFactory.getLogger(JndiManager::class.java)
    }

    @ShellMethod("Add Database Connection")
    fun addConnection(
        jndi: String,
        @ShellOption(valueProvider = ContextValueProvider::class) context: String,
        url: String,
        driver: String,
        user: String,
        password: String
    ) {

        val params = HashMap<String, String>()
        params["type"] = "javax.sql.DataSource"
        params["url"] = url
        params["driver"] = driver
        params["user"] = user
        params["password"] = password

        try {
            val success = JNDIUtils.addJndiConnection(jndi, context, params)
            if (success) {
                logger.info(
                    "Jndi entry $jndi for context $context successfully added.".trimMargin()
                )
            } else {
                logger.error("There was error entering jndi entry. Please check the log.")
            }
        } catch (e: Exception) {
            logger.error("Exception occurred while adding jndi entry: ${e.message}")
            throw e
        }
    }
}