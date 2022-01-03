package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.db.metadata.dto.Table

data class GetAllTables(val entries: List<Table>): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any?>> = mutableListOf(mutableMapOf("Table Name" to "Table Name", "Table Type" to "Table Type"))
        entries.forEach {entry ->
            values.add(mutableMapOf("Table Name" to entry.tableName, "Table Type" to entry.tableType))
        }
        return Either.right(values)
    }
}