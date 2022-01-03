package org.dbshell.actions.connection

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.GridResult
import org.dbshell.db.metadata.DatabasePrimitives

//TODO: Make attribute string a list of strings
data class GetActiveConnectionInfo(val attribute: String? = null): Action {
    override fun execute(): ActionResult {
        val headers = setOf("Connection Property", "Value")
        var entries = DatabasePrimitives.getFormattedPrimitivesFromDbMetadata()
        entries = if(attribute != null) {
            entries.filter {ce -> ce.key == attribute}.toSet()
        } else {
            entries
        }
        val result = entries.map {e -> listOf(e.key, e.value) }
        val gridResult = GridResult(headers, result)
        return Either.right(gridResult)
    }
}