package org.dbshell.commands.connections

import org.dbshell.actions.sql.ExportQueryToCsv
import org.dbshell.actions.sql.RunQuery
import org.dbshell.actions.sql.RunSqlCommands

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.io.File
//'"'
@ShellComponent
class SqlManager: UIManager {
    @ShellMethod("Run a SQL query")
    fun runQuery(sql: String, rowLimit: Long =  50, @ShellOption(defaultValue = "false") executeAsync: Boolean) {
        val rq = RunQuery(sql, rowLimit)
        executeAction(rq, executeAsync)
    }
    @ShellMethod("Export SQL query to csv")
    fun exportQueryToCsv(
        sql: String,
        outputFile: File,
        @ShellOption(defaultValue = ",") separator: String,
        @ShellOption(defaultValue = "\"") quoteChar: String,
        @ShellOption(defaultValue = "\"") escapeChar: String,
        @ShellOption(defaultValue = "\n") lineEndChar: String,
        @ShellOption(defaultValue = "true") includeHeaders: Boolean,
        @ShellOption(defaultValue = "false") executeAsync: Boolean
    )  {
        val exportCsv =
            ExportQueryToCsv(sql, outputFile, separator, quoteChar, escapeChar, lineEndChar, includeHeaders)

        executeAction(exportCsv, executeAsync)
    }
    @ShellMethod("Run SQL commands")
    fun runSqlCommands(sql: String, @ShellOption(defaultValue = "false") executeAsync: Boolean) {
        val runSqlCommands = RunSqlCommands(sql)
        executeAction(runSqlCommands, executeAsync)
    }
}