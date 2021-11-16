package org.dbshell.actions.sql

import io.vavr.control.Either
import org.bradfordmiller.sqlutils.QueryInfo
import org.bradfordmiller.sqlutils.SqlUtils
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.commands.connections.dto.ConnectionInfoUtil

data class RunQuery(val sql: String, val rowLimit: Long =  50): Action {
    override fun execute(): ActionResult {
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
        return Either.right(values.map { v -> v.values.toTypedArray()}.toTypedArray())
    }
}