package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.GridResult
import org.dbshell.db.metadata.dto.Table

data class GetAllTables(val entries: List<Table>): Action {
    override fun execute(): ActionResult {
        val headers = setOf("Table Name", "Table Type")
        val result = entries.map {e -> listOf(e.tableName, e.tableType)}
        val gridResult = GridResult(headers, result)
        return Either.right(gridResult)
    }
}