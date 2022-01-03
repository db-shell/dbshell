package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.db.metadata.dto.Catalog

data class GetAllCatalogs(val entries: List<Catalog>): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any?>> = mutableListOf(mutableMapOf("Catalog" to "Catalog"))
        entries.forEach {entry ->
            values.add(mutableMapOf("Catalog" to entry.toString()))
        }
        return Either.right(values)
    }
}