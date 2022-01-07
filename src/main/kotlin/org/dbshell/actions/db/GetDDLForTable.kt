package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.ActionResult
import org.dbshell.actions.UIAction

data class GetDDLForTable(val entries:  List<String>): UIAction() {
    override val headers: MutableSet<String>
        get() = mutableSetOf("SQL Script")

    override fun execute(): ActionResult {
        val result = entries.map {e -> listOf(e)}
        val gridResult = getGridResult(result)
        return Either.right(gridResult)
    }
}