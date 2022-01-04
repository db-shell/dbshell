package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.ActionResult
import org.dbshell.actions.UIAction
import org.dbshell.db.metadata.dto.Table

data class GetAllTables(val entries: List<Table>): UIAction() {
    override val headers: MutableSet<String>
        get() = mutableSetOf("Table Name", "Table Type")

    override fun execute(): ActionResult {
        val result = entries.map {e -> listOf(e.tableName, e.tableType)}
        val gridResult = getGridResult(result)
        return Either.right(gridResult)
    }
}