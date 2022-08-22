package org.dbshell.actions.sql

import com.opencsv.CSVWriter
import io.vavr.control.Either
import org.apache.spark.sql.SparkSession
import org.dbshell.actions.Action
import org.dbshell.actions.ActionLog
import org.dbshell.actions.ActionResult
import org.dbshell.reflection.utils.DatabaseMetadataUtil
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import java.io.File
import java.io.PrintWriter
import java.util.*

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
        val spark =
            SparkSession
                .builder()
                .master("local[2]")
                .appName("db-shell - query to file")
                .orCreate

        val credentials = ConnectionInfoUtil.getCredentialsFromCurrentConnection()

        val connectionProperties = Properties()
        connectionProperties["user"] = credentials.username
        connectionProperties["password"] = credentials.password

        val readDf = spark.read().jdbc(credentials.url, sql, connectionProperties)
        readDf
            .write()
            .option("header", includeHeaders)
            .option("delimiter", separator)
            .option("quote", quoteChar)
            .option("escape", escapeChar)
            .option("lineSep", lineEndChar)
            .csv(outputFile.path)

        actionList.add(ActionLog("Export complete."))
        return Either.left(actionList)
    }
}