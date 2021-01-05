package org.dbshell.commands.connections

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.Driver
import org.slf4j.LoggerFactory
import javax.validation.Valid

@ShellComponent
class JndiManager {

    companion object {
        private val logger = LoggerFactory.getLogger(JndiManager::class.java)
    }

    @ShellMethod("Add Database Connection")
    fun addConnection(jndi: String, context: String, url: String, driver: String, user: String, password: String) {

        val params = HashMap<String, String>()
        params.put("type", "javax.sql.DataSource")
        params.put("url", url)
        params.put("driver", driver)
        params.put("user", user)
        params.put("password", password)

        try {
            val success = JNDIUtils.addJndiConnection(jndi, context, params)
            if (success) {
                logger.info(
                    """Jndi entry ${jndi} for context ${context}
                            |successfully added.""".trimMargin()
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