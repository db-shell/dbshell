package org.dbshell.commands.connections

import org.bradfordmiller.sqlutils.QueryInfo
import org.bradfordmiller.sqlutils.SqlUtils
import org.dbshell.actions.ExportQueryToCsv
import org.dbshell.actions.RunQuery
import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.jobqueue.JobQueue
import org.dbshell.jobqueue.JobQueueWrapper
import org.dbshell.jobqueue.ResultQueueWrapper
import org.dbshell.jobqueue.ResultsQueueConsumer
import org.dbshell.results.ResultsHashMap
import org.dbshell.ui.TablesUtil
import org.dbshell.utils.ScriptRunner
import org.relique.jdbc.csv.CsvDriver
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import java.io.File
import java.io.PrintStream

@ShellComponent
class SqlManager: UIManager {
    @ShellMethod("Run a SQL query")
    fun runQuery(sql: String, rowLimit: Long =  50, executeAsync: Boolean = false) {
        val rq = RunQuery(sql, rowLimit)
        val jobId = JobQueueWrapper.put(rq)
        val result = ResultsHashMap.resultsMap[jobId]!!
        renderResult(result)
    }
    @ShellMethod("Export SQL query to csv")
    fun exportQueryToCsv(sql: String, outputFile: File, separator: String = ",", includeHeaders: Boolean = true, fileExtension: String = ".csv", executeAsync: Boolean = false)  {
        //println("Executing query '$sql' and exporting results to output file ${outputFile.absolutePath}...")
        //val exportCsv = ExportQueryToCsv(sql, outputFile, separator, includeHeaders, fileExtension)
        //JobQueueWrapper.put(exportCsv)
        //processAction(exportCsv, executeAsync)
        //println("Export complete.")
    }
    @ShellMethod("Run SQL commands")
    fun runSqlCommands(sql: String) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            ScriptRunner.executeScript(sql, connection)
        }
    }
}