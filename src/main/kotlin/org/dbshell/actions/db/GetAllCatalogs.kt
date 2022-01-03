package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.GridResult
import org.dbshell.db.metadata.dto.Catalog

data class GetAllCatalogs(val entries: List<Catalog>): Action {
    override fun execute(): ActionResult {
        val headers = setOf("Catalog")
        val result = entries.map {e -> listOf(e.toString())}
        val gridResult = GridResult(headers, result)
        return Either.right(gridResult)
    }
}