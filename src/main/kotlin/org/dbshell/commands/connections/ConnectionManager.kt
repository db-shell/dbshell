package org.dbshell.commands.connections

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.environment.EnvironmentVars
import org.dbshell.reflection.utils.DatabaseMetadataUtil
import org.dbshell.ui.TablesUtil
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.sql.Connection
import java.sql.SQLException
import java.util.*

@ShellComponent
class ConnectionManager {

    data class ConnectionEntries(val key: String, val value: String)

    val connectionHeaders = LinkedHashMap<String, Any>()
    val schemaHeaders = LinkedHashMap<String, Any>()
    val catalogHeaders = LinkedHashMap<String, Any>()

    init {
        connectionHeaders["key"] = "Connection Property"
        connectionHeaders["value"] = "Value"

        schemaHeaders["key"] = "Schema"
        schemaHeaders["value"] = "Catalog"

        catalogHeaders["key"] = "Catalog"
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionManager::class.java)

        private fun getConnectionFromCurrentContextJndi(): Triple<String, String, Connection> {
            val (envContext, envJndi) = EnvironmentVars.getCurrentContextAndJndi()
            return Triple(envContext, envJndi,  JNDIUtils.getJndiConnection(envJndi, envContext))
        }

        private fun getFormattedPrimitivesFromDbMetadata(): Set<ConnectionEntries> {

            val (envContext, envJndi, connection) = getConnectionFromCurrentContextJndi()

            return try {

                val dbmd = connection.metaData
                val methodList = DatabaseMetadataUtil.getPrimitivesFromDBMetadata(dbmd)

                methodList.map { m ->
                    val retval =
                        try {
                            m.value.invoke(dbmd)
                        } catch (ex: Exception) {
                            "Not Supported by this driver"
                        }

                    if (retval == null)
                        ConnectionEntries(m.key, "")
                    else {
                        val formattedRetval =
                            if (retval.toString().length > 77) {
                                retval.toString().substring(0, 77) + "..."
                            } else {
                                retval.toString()
                            }
                        ConnectionEntries(m.key, formattedRetval)
                    }
                }.sortedBy { e -> e.key }.toSet()
            } catch (sqlEx: SQLException) {
                val message =
                    "Error when creating connection to context $envContext and jndi $envJndi: ${sqlEx.message}"
                logger.error(message)
                throw sqlEx
            }
        }
    }

    @ShellMethod("Get active connection information")
    fun getCurrentConnectionInfo() {
        try {
            val entries = getFormattedPrimitivesFromDbMetadata()
            TablesUtil.renderAttributeTable(connectionHeaders, entries)
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }

    @ShellMethod("Get specific attribute of active connection information")
    fun getCurrentConnectionAttributeInfo(@ShellOption(valueProvider = DatabaseMdPrimitiveProvider::class) attributeName: String) {
        try {
            val entries = getFormattedPrimitivesFromDbMetadata()
            val entryAttribute = entries.filter {ce -> ce.key == attributeName}
            TablesUtil.renderAttributeTable(connectionHeaders, entryAttribute)
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }

    @ShellMethod("List Schemas in active connections database")
    fun getAllSchemas() {
        try {
            val (_, _, connection) = getConnectionFromCurrentContextJndi()
            val dbmd = connection.metaData
            val entries = DatabaseMetadataUtil.getSchemas(dbmd)
            TablesUtil.renderAttributeTable(schemaHeaders, entries)
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }

    @ShellMethod("List all catalogs in active connections database")
    fun getAllCatalogs() {
        val (_, _, connection) = getConnectionFromCurrentContextJndi()
        val dbmd = connection.metaData
        val entries = DatabaseMetadataUtil.getCatalogs(dbmd)
        TablesUtil.renderAttributeTable(catalogHeaders, entries)
    }
}