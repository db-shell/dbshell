package org.dbshell.actions

import io.vavr.control.Either
import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.relique.jdbc.csv.CsvDriver
import java.io.File
import java.io.PrintStream
import java.util.*

data class ExportQueryToCsv(val sql: String, val outputFile: File, val separator: String = ",", val includeHeaders: Boolean = true, val fileExtension: String = ".csv", val runAsync: Boolean = false): Action {
    override fun execute(): ActionResult {
        var actionList = mutableListOf<ActionLog>()
        actionList.add(ActionLog("Executing query '$sql' and exporting results to output file ${outputFile.absolutePath}..."))
        val conn = ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection
        val stmt = conn.createStatement()
        stmt?.executeQuery(sql).use {rs ->
            PrintStream(outputFile).use { ps ->
                CsvDriver.writeToCsv(rs,ps,includeHeaders)
            }
        }
        actionList.add(ActionLog("Export complete."))
        return Either.left(actionList)
    }
}