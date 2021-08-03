package org.dbshell.commands.connections

import org.bradfordmiller.sqlutils.QueryInfo
import org.bradfordmiller.sqlutils.SqlUtils
import org.dbshell.actions.ExportQueryToCsv
import org.dbshell.actions.RunQuery
import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.jobqueue.JobQueue
import org.dbshell.jobqueue.QueueWrapper
import org.dbshell.ui.TablesUtil
import org.dbshell.utils.ScriptRunner
import org.relique.jdbc.csv.CsvDriver
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import java.io.File
import java.io.PrintStream

@ShellComponent
class SqlManager: Manager {
    @ShellMethod("Run a SQL query")
    fun runQuery(sql: String, rowLimit: Long =  50, executeAsync: Boolean = false) {
        val rq = RunQuery(sql, rowLimit)
        processAction(rq, executeAsync)
    }
    @ShellMethod("Export SQL query to csv")
    fun exportQueryToCsv(sql: String, outputFile: File, separator: String = ",", includeHeaders: Boolean = true, fileExtension: String = ".csv", executeAsync: Boolean = false)  {
        //println("Executing query '$sql' and exporting results to output file ${outputFile.absolutePath}...")
        val exportCsv = ExportQueryToCsv(sql, outputFile, separator, includeHeaders, fileExtension)
        processAction(exportCsv, executeAsync)
        //println("Export complete.")
    }
    @ShellMethod("Run SQL commands")
    fun runSqlCommands(sql: String) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            ScriptRunner.executeScript(sql, connection)
        }
    }
}