package org.dbshell.shellmethods

import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.dbshell.reflection.utils.DatabaseMetadataUtil
import org.dbshell.providers.DatabaseMdPrimitiveProvider
import org.dbshell.ui.TablesUtil
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.sql.SQLException
import java.util.*

@ShellComponent
class ConnectionMethods {

    data class ConnectionEntries(val key: String, val value: String)

    val connectionHeaders = LinkedHashMap<String, Any>()
    val schemaHeaders = LinkedHashMap<String, Any>()
    val catalogHeaders = LinkedHashMap<String, Any>()

    init {
        connectionHeaders["key"] = "Connection Property"
        connectionHeaders["value"] = "Value"
        schemaHeaders["schema"] = "Schema"
        catalogHeaders["catalog"] = "Catalog"
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionMethods::class.java)

        private fun getFormattedPrimitivesFromDbMetadata(): Set<ConnectionEntries> {

            val connectionInfo = ConnectionInfoUtil.getConnectionFromCurrentContextJndi()

            return try {

                connectionInfo.connection.use { connection ->

                    val dbmd = connection.metaData
                    val methodList = DatabaseMetadataUtil.getPrimitivesFromDBMetadata(dbmd)

                    methodList.map { m ->
                        val retval =
                            try {
                                m.value?.invoke(dbmd)
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
                }
            } catch (sqlEx: SQLException) {
                val message =
                    "Error when creating connection to context ${connectionInfo.contextName} and jndi ${connectionInfo.jndiName}: ${sqlEx.message}"
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
            ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use {connection ->
                val dbmd = connection.metaData
                val entries = DatabaseMetadata.getSchemas(dbmd)
                TablesUtil.renderAttributeTable(schemaHeaders, entries)
            }
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }

    @ShellMethod("List all catalogs in active connections database")
    fun getAllCatalogs() {
        try {
            ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
                val dbmd = connection.metaData
                val entries = DatabaseMetadata.getCatalogs(dbmd)
                TablesUtil.renderAttributeTable(catalogHeaders, entries)
            }
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }
}