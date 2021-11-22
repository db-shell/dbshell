package org.dbshell.actions.sql

import com.opencsv.CSVWriter
import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionLog
import org.dbshell.actions.ActionResult
import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.relique.jdbc.csv.CsvDriver
import org.relique.jdbc.csv.CsvResultSet
import java.io.File
import java.io.PrintStream
import java.io.PrintWriter
import java.io.StringWriter

data class ExportQueryToCsv(
    val sql: String,
    val outputFile: File,
    val separator: String,
    val quoteChar: String,
    val escapeChar: String,
    val lineEndChar: String,
    val includeHeaders: Boolean = true,
    val runAsync: Boolean = false
): Action {
    override fun execute(): ActionResult {
        var actionList = mutableListOf<ActionLog>()
        actionList.add(
            ActionLog("Executing query '$sql' and exporting results to output file ${outputFile.absolutePath}...")
        )
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use {conn ->
            conn.createStatement().use {stmt ->
                stmt?.executeQuery(sql).use { rs ->
                    PrintWriter(outputFile).use { pw ->
                        val csvWriter =
                            CSVWriter(pw, separator.first(), quoteChar.first(), escapeChar.first(), lineEndChar)

                        csvWriter.writeAll(rs, includeHeaders)
                    }
                }
            }
        }
        actionList.add(ActionLog("Export complete."))
        return Either.left(actionList)
    }
}