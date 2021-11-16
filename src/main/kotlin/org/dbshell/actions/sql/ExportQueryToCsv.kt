package org.dbshell.actions.sql

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionLog
import org.dbshell.actions.ActionResult
import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.relique.jdbc.csv.CsvDriver
import java.io.File
import java.io.PrintStream

data class ExportQueryToCsv(val sql: String, val outputFile: File, val separator: String = ",", val includeHeaders: Boolean = true, val fileExtension: String = ".csv", val runAsync: Boolean = false): Action {
    override fun execute(): ActionResult {
        var actionList = mutableListOf<ActionLog>()
        actionList.add(ActionLog("Executing query '$sql' and exporting results to output file ${outputFile.absolutePath}..."))
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use {conn ->
            conn.createStatement().use {stmt ->
                stmt?.executeQuery(sql).use { rs ->
                    PrintStream(outputFile).use { ps ->
                        CsvDriver.writeToCsv(rs, ps, includeHeaders)
                    }
                }
            }
        }
        actionList.add(ActionLog("Export complete."))
        return Either.left(actionList)
    }
}