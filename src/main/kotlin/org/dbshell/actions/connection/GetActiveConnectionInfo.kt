package org.dbshell.actions.connection

import io.vavr.control.Either
import org.dbshell.actions.ActionResult
import org.dbshell.actions.UIAction
import org.dbshell.db.metadata.DatabasePrimitives

//TODO: Make attribute string a list of strings
data class GetActiveConnectionInfo(val attribute: String? = null): UIAction() {
    override val headers: MutableSet<String>
        get() = mutableSetOf("Connection Property", "Value")

    override fun execute(): ActionResult {
        var entries = DatabasePrimitives.getFormattedPrimitivesFromDbMetadata()
        entries = if(attribute != null) {
            entries.filter {ce -> ce.key == attribute}.toSet()
        } else {
            entries
        }
        val result = entries.map {e -> listOf(e.key, e.value) }
        val gridResult = getGridResult(result)
        return Either.right(gridResult)
    }
}