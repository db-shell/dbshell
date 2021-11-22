package org.dbshell.shellmethods

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.ui.TablesUtil
import org.dbshell.providers.ContextValueProvider
import org.dbshell.providers.JndiValueProvider
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.*
import javax.naming.InitialContext

import java.util.*

@ShellComponent
class JndiMethods {

    data class JndiEntries(val key: String, val value: String)

    companion object {
        private val logger = LoggerFactory.getLogger(JndiMethods::class.java)
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

    @ShellMethod("List database entries from a context")
    fun listEntries(@ShellOption(valueProvider = ContextValueProvider::class) context: String) {
        try {
            val initCtx = InitialContext()
            val mc = JNDIUtils.getMemoryContextFromInitContext(initCtx, context)
            val entries = JNDIUtils.getEntriesForJndiContext(mc).map{kvp -> JndiEntries(kvp.key, kvp.value) }.toList()

            val headers = LinkedHashMap<String, Any>()
            headers["key"] = "Jndi Entry"
            headers["value"] = "Value"

            TablesUtil.renderAttributeTable(headers, entries)

        } catch(e: Exception) {
            logger.error("Error when accessing entries for context $context: ${e.message}")
            throw e
        }
    }

    @ShellMethod("Get details for a context and jndi")
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
    }

    @ShellMethod("Set the current active shell connection with a context and jndi")
    fun setActiveConnection(
        @ShellOption(valueProvider = ContextValueProvider::class) context: String,
        @ShellOption(valueProvider = JndiValueProvider::class) jndi: String
    ) {
        EnvironmentVars.currentContextAndJndi(context, jndi)
        EnvironmentProps.setCurrentContextandJndi(context, jndi)
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
            val message = "Error when creating connection to context ${contextAndJndi.context} and jndi ${contextAndJndi.jndi}: ${sqlEx.message}"
            logger.error(message)
        }
    }
}