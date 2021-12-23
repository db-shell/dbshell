package org.dbshell.actions.connection

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.UIArrayResult
import org.dbshell.actions.UIBeanArrayResult
import org.dbshell.db.metadata.DatabasePrimitives
import java.util.LinkedHashMap

data class GetActiveConnectionInfo(private val attribute: String? = null): Action {
    override fun execute(): ActionResult {
        var values: MutableList<Map<String, Any>> = mutableListOf(mutableMapOf("Connection Property" to "Connection Property", "Value" to "Value"))
        var entries = DatabasePrimitives.getFormattedPrimitivesFromDbMetadata()
        entries = if(attribute != null) {
            entries.filter {ce -> ce.key == attribute}.toSet()
        } else {
            entries
        }
        entries.forEach {entry ->
            values.add(mutableMapOf("Connection Property" to entry.key, "Value" to entry.value))
        }
        val data = UIArrayResult(values.map { v -> v.values.toTypedArray()}.toTypedArray())
        return Either.right(data)
    }
}