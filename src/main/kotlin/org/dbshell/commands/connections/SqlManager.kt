package org.dbshell.commands.connections

import org.dbshell.actions.sql.ExportQueryToCsv
import org.dbshell.actions.sql.RunQuery
import org.dbshell.actions.sql.RunSqlCommands

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import java.io.File

@ShellComponent
class SqlManager: UIManager {
    @ShellMethod("Run a SQL query")
    fun runQuery(sql: String, rowLimit: Long =  50, executeAsync: Boolean = false) {
        val rq = RunQuery(sql, rowLimit)
        executeAction(rq, executeAsync)
    }
    @ShellMethod("Export SQL query to csv")
    fun exportQueryToCsv(sql: String, outputFile: File, separator: String = ",", includeHeaders: Boolean = true, fileExtension: String = ".csv", executeAsync: Boolean = false)  {
        val exportCsv = ExportQueryToCsv(sql, outputFile, separator, includeHeaders, fileExtension)
        executeAction(exportCsv, executeAsync)
    }
    @ShellMethod("Run SQL commands")
    fun runSqlCommands(sql: String, executeAsync: Boolean) {
        val runSqlCommands = RunSqlCommands(sql)
        executeAction(runSqlCommands, executeAsync)
    }
}