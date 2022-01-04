package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.GridResult
import org.dbshell.actions.UIAction
import org.dbshell.db.metadata.dto.Schema

data class GetAllSchemas(val entries: List<Schema>): UIAction() {
    override val headers: MutableSet<String>
        get() = mutableSetOf("Schema")

    override fun execute(): ActionResult {
        val result = entries.map{e -> listOf(e.toString())}
        val gridResult = getGridResult(result)
        return Either.right(gridResult)
    }
}