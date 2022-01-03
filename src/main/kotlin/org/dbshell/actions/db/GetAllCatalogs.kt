package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.GridResult
import org.dbshell.actions.UIAction
import org.dbshell.db.metadata.dto.Catalog

data class GetAllCatalogs(val entries: List<Catalog>): UIAction() {
    override val headers: Set<String>
        get() = setOf("Catalog")

    override fun execute(): ActionResult {
        val result = entries.map {e -> listOf(e.toString())}
        val gridResult = getGridResult(result)
        return Either.right(gridResult)
    }
}