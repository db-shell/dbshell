package org.dbshell.shellmethods

import org.dbshell.actions.ActionExecutor
import org.dbshell.actions.connection.GetActiveConnectionInfo
import org.dbshell.actions.db.GetAllCatalogs
import org.dbshell.actions.db.GetAllSchemas
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.dbshell.providers.DatabaseMdPrimitiveProvider
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.util.*

@ShellComponent
class ConnectionMethods: ActionExecutor {

    private final val schemaHeaders = LinkedHashMap<String, Any>()

    init {
        schemaHeaders["schema"] = "Schema"
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
                val getSchemas = GetAllSchemas(connection.metaData)
                val result = executeAction(getSchemas)
                renderResult(result)
            }
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }

    @ShellMethod("List all catalogs in active connections database")
    fun getAllCatalogs() {
        try {
            ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
                val getCatalogs = GetAllCatalogs(connection.metaData)
                val result = executeAction(getCatalogs)
                renderResult(result)
            }
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }
}