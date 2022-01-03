package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.db.metadata.dto.Schema

data class GetAllSchemas(val entries: List<Schema>): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any?>> = mutableListOf(mutableMapOf("Schema" to "Schema"))
        entries.forEach {entry ->
            values.add(mutableMapOf("Schema" to entry.toString()))
        }
        return Either.right(values)
    }
}