package org.dbshell.shellmethods

import org.dbshell.actions.ActionExecutor
import org.dbshell.actions.jndi.GetEntries
import org.dbshell.connections.ConnectionRepository
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.providers.ConnectionValueProvider
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.*
import javax.naming.InitialContext

import java.util.*

@ShellComponent
class ConnectionMethods: ActionExecutor {

    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionMethods::class.java)
    }

    @ShellMethod("Add Database Connection")
    fun addConnection(
        name: String,
        url: String,
        user: String,
        password: String
    ) {

        try {
            val success = ConnectionRepository.addConnection(name, url, user, password)  //JNDIUtils.addJndiConnection(jndi, context, params)
            if (success > 0) {
                logger.info(
                    "Connection entry $name was successfully added.".trimMargin()
                )
            } else {
                logger.error("There was error entering connection entry. Please check the log.")
            }
        } catch (e: Exception) {
            logger.error("Exception occurred while adding connection entry: ${e.message}")
            throw e
        }
    }

    @ShellMethod("List database entries")
    fun listEntries(
        @ShellOption(defaultValue = "false") executeAsync: Boolean
    ) {
        try {
            val getEntries = GetEntries()
            val result = executeAction(getEntries, executeAsync)
            renderResult(result)
        } catch(e: Exception) {
            logger.error("Error when accessing database entries: ${e.message}")
            throw e
        }
    }

    /*@ShellMethod("Get details for a context and jndi")
    fun getDetails(
        @ShellOption(valueProvider = ContextValueProvider::class) context: String,
        @ShellOption(valueProvider = JndiValueProvider::class) jndi: String
        ) {
        try {
            val initCtx = InitialContext()
            val entry = JNDIUtils.getDetailsforJndiEntry(initCtx, context, jndi)
            println(entry)
        } catch(e: Exception) {
            logger.error("Error when getting details for context $context and jndi $jndi: ${e.message}")
            throw e
        }
    }*/

    @ShellMethod("Set the current active shell connection with a context and jndi")
    fun setActiveConnection(
        @ShellOption(valueProvider = ConnectionValueProvider::class) name: String
    ) {
        //EnvironmentVars.currentContextAndJndi(context, jndi)
        //EnvironmentProps.setCurrentContextandJndi(context, jndi)
        EnvironmentVars.currentConnectionName
        EnvironmentVars.currentCatalog = ""
        EnvironmentProps.setCurrentCatalog("")
        EnvironmentVars.currentSchema = ""
        EnvironmentProps.setCurrentSchema("")
        println("Set current connection to context $context and jndi $jndi")
    }

    @ShellMethod("Validate the active connection")
    fun validateActiveConnection() {
        val contextAndJndi = EnvironmentVars.currentContextAndJndi
        try {
            val ds = JNDIUtils.getDataSource(contextAndJndi.jndi, contextAndJndi.context).left
            ds.connection
            println("Successfully validated current active connection.")
        } catch (sqlEx: Exception) {
            val message =
                "Error when creating connection to context ${contextAndJndi.context} and jndi ${contextAndJndi.jndi}: ${sqlEx.message}"
            logger.error(message)
        }
    }
}