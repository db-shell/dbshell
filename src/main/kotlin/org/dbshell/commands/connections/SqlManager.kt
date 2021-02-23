package org.dbshell.commands.connections

import org.bradfordmiller.sqlutils.QueryInfo
import org.bradfordmiller.sqlutils.SqlUtils
import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.ui.TablesUtil
import org.relique.jdbc.csv.CsvDriver
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import java.io.File
import java.io.PrintStream

@ShellComponent
class SqlManager {
    @ShellMethod("Run a SQL query")
    fun runQuery(sql: String, rowLimit: Long =  50) {
        var rowCount = 0L
        lateinit var values: MutableList<Map<String, Any>>
        lateinit var qi: QueryInfo
        lateinit var columns: Map<Int, String>
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            try {
                qi = SqlUtils.getQueryInfo(sql, connection)
                columns = SqlUtils.getColumnsFromRs(qi)
                values = mutableListOf(columns.values.map{c -> c to c as Any}.toMap())
                connection.createStatement().use { stmt ->
                    stmt.executeQuery(sql).use { rs ->
                        while (rs.next() && rowCount <= rowLimit) {
                            values.add(SqlUtils.getMapFromRs(rs, columns).toMutableMap())
                            rowCount += 1
                        }
                    }
                }
            } catch(e: Exception) {
                println(e.message)
            }
        }
        val columnValues = values.map {v -> v.values.toTypedArray()}.toTypedArray()
        TablesUtil.renderAttributeTable(columnValues)
    }
    @ShellMethod("Export SQL query to csv")
    fun exportQueryToCsv(sql: String, outputFile: File, separator: String = ",", includeHeaders: Boolean = true, fileExtension: String = ".csv")  {
        println("Executing query '$sql' and exporting results to output file ${outputFile.absolutePath}...")
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            connection.createStatement().use { stmt ->
                stmt.executeQuery(sql).use { rs ->
                    PrintStream(outputFile).use { ps ->
                        CsvDriver.writeToCsv(rs,ps,includeHeaders)
                    }
                }
            }
        }
        println("Export complete.")
    }
}