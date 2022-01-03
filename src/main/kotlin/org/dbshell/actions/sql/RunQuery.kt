package org.dbshell.actions.sql

import io.vavr.control.Either
import org.bradfordmiller.sqlutils.QueryInfo
import org.bradfordmiller.sqlutils.SqlUtils
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.GridResult
import org.dbshell.shellmethods.dto.ConnectionInfoUtil

data class RunQuery(val sql: String, val rowLimit: Long =  50): Action {
    override fun execute(): ActionResult? {
        var rowCount = 0L
        try {
            ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
                val values: MutableList<Map<String, Any?>> = mutableListOf()
                val qi = SqlUtils.getQueryInfo(sql, connection)
                val columns = SqlUtils.getColumnsFromRs(qi)
                val headers = columns.values.toSet()
                connection.createStatement().use { stmt ->
                    stmt.executeQuery(sql).use { rs ->
                        while (rs.next() && rowCount <= rowLimit) {
                            values.add(SqlUtils.getMapFromRs(rs, columns).toMutableMap())
                            rowCount += 1
                        }
                    }
                }
                val entries = values.map { v -> v.values.toList() }
                val gridResult = GridResult(headers, entries)
                return Either.right(gridResult)
            }
        } catch (e: Exception) {
            println(e.message)
            return null
        }
    }
}