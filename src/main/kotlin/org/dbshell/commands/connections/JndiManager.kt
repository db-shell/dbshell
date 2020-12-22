package org.dbshell.commands.connections

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.Driver
import org.dbshell.jcommander.commands.CommandJndi
import org.dbshell.jcommander.parameters.AddJndi
import org.slf4j.LoggerFactory
import javax.validation.Valid

@ShellComponent
class JndiManager {

    companion object {
        private val logger = LoggerFactory.getLogger(JndiManager::class.java)
    }

    @ShellMethod("Add a new JDBC connection to db-shell", key=["jndi"])
    fun addEntry(@ShellOption(optOut=true) @Valid commandJndi: CommandJndi) {
        if(commandJndi.addJndi != null) {
            try {
                val success =
                    JNDIUtils.addJndiConnection(
                        commandJndi.addJndi!!.jndiName, commandJndi.addJndi!!.contextName, commandJndi.params
                    )
                if(success) {
                    logger.info(
                        """Jndi entry ${commandJndi.addJndi!!.jndiName} for context ${commandJndi.addJndi!!.contextName}
                            |successfully added.""".trimMargin()
                    )
                } else {
                    logger.error("There was error entering jndi entry. Please check the log.")
                }
            } catch(e: Exception) {
                logger.error("Exception occurred while adding jndi entry: ${e.message}")
                throw e
            }
        }
    }
}