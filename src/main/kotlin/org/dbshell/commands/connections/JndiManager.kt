package org.dbshell.commands.connections

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.*
import javax.naming.InitialContext
import org.springframework.shell.table.TableBuilder

import org.springframework.shell.table.BeanListTableModel
import org.springframework.shell.table.BorderStyle
import java.util.*

@ShellComponent
class JndiManager {

    data class JndiEntries(val key: String, val value: String)

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

    @ShellMethod("List database entries from a context")
    fun listEntries(@ShellOption(valueProvider = ContextValueProvider::class) context: String) {
        try {
            val initCtx = InitialContext()
            val mc = JNDIUtils.getMemoryContextFromInitContext(initCtx, context)
            val entries = JNDIUtils.getEntriesForJndiContext(mc).map{kvp -> JndiEntries(kvp.key, kvp.value)}.toList()

            val headers = LinkedHashMap<String, Any>()
            headers["key"] = "Jndi Entry"
            headers["value"] = "Value"

            val model = BeanListTableModel(entries, headers)
            val tableBuilder = TableBuilder(model)
            tableBuilder.addInnerBorder(BorderStyle.fancy_light)
            tableBuilder.addHeaderBorder(BorderStyle.fancy_double)

            println(tableBuilder.build().render(80))

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
        EnvironmentVars.setCurrentContextAndJndi(context, jndi)
        EnvironmentProps.setCurrentContextandJndi(context, jndi)
        println("Set current connection to context $context and jndi $jndi")
    }

    @ShellMethod("Validate the active connection")
    fun validateActiveConnection() {
        val (envContext, envJndi) = EnvironmentVars.getCurrentContextAndJndi()
        try {
            val ds = JNDIUtils.getDataSource(envJndi, envContext).left
            ds.connection
            println("Successfully validated current active connection.")
        } catch (sqlEx: Exception) {
            val message = "Error when creating connection to context $envContext and jndi $envJndi: ${sqlEx.message}"
            logger.error(message)
        }
    }
}