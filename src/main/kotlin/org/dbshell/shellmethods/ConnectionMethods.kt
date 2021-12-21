package org.dbshell.shellmethods

import org.dbshell.actions.ActionExecutor
import org.dbshell.actions.connection.GetActiveConnectionInfo
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.dbshell.providers.DatabaseMdPrimitiveProvider
import org.dbshell.ui.TablesUtil
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.util.*

@ShellComponent
class ConnectionMethods: ActionExecutor {

    private final val schemaHeaders = LinkedHashMap<String, Any>()
    private final val catalogHeaders = LinkedHashMap<String, Any>()

    init {
        schemaHeaders["schema"] = "Schema"
        catalogHeaders["catalog"] = "Catalog"
    }

    @ShellMethod("Get active connection information")
    fun getCurrentConnectionInfo(@ShellOption(defaultValue = "false") executeAsync: Boolean) {
        try {
            val getActiveConnectionInfo = GetActiveConnectionInfo()
            val result = executeAction(getActiveConnectionInfo, executeAsync)
            renderResult(result)
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }

    @ShellMethod("Get specific attribute of active connection information")
    fun getCurrentConnectionAttributeInfo(
        @ShellOption(valueProvider = DatabaseMdPrimitiveProvider::class) attributeName: String,
        @ShellOption(defaultValue = "false") executeAsync: Boolean
        ) {
        try {
            val getActiveConnectionInfo = GetActiveConnectionInfo(attributeName)
            val result = executeAction(getActiveConnectionInfo, executeAsync)
            renderResult(result)
        } catch (ex: Exception) {
            println("Error getting current connection attribute information: ${ex.message}")
        }
    }

    @ShellMethod("List Schemas in active connections database")
    fun getAllSchemas() {
        try {
            ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
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