package org.dbshell.actions.connection

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.db.metadata.DatabasePrimitives

//TODO: Make attribute string a list of strings
data class GetActiveConnectionInfo(val attribute: String? = null): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any?>> = mutableListOf(mutableMapOf("Connection Property" to "Connection Property", "Value" to "Value"))
        var entries = DatabasePrimitives.getFormattedPrimitivesFromDbMetadata()
        entries = if(attribute != null) {
            entries.filter {ce -> ce.key == attribute}.toSet()
        } else {
            entries
        }
        entries.forEach {entry ->
            values.add(mutableMapOf("Connection Property" to entry.key, "Value" to entry.value))
        }
        return Either.right(values)
    }
}