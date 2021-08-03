package org.dbshell.actions

import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.relique.jdbc.csv.CsvDriver
import java.io.File
import java.io.PrintStream
import java.sql.ResultSet

data class ExportQueryToCsv(val sql: String, val outputFile: File, val separator: String = ",", val includeHeaders: Boolean = true, val fileExtension: String = ".csv"): Action<ResultSet> {
    override fun execute(): ResultSet {
        val conn = ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection
        val stmt = conn.createStatement()
        return stmt.executeQuery(sql)
    }
    override fun render(data: ResultSet) {
        data.use { rs ->
            PrintStream(outputFile).use { ps ->
                CsvDriver.writeToCsv(rs,ps,includeHeaders)
            }
        }
    }
}