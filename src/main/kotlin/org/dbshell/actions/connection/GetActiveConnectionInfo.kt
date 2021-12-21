package org.dbshell.actions.connection

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.UIBeanArrayResult
import org.dbshell.db.metadata.DatabasePrimitives
import java.util.LinkedHashMap

class GetActiveConnectionInfo(private val attribute: String? = null): Action {

    val connectionHeaders = LinkedHashMap<String, Any>()

    init {
        connectionHeaders["key"] = "Connection Property"
        connectionHeaders["value"] = "Value"
    }

    override fun execute(): ActionResult {
        var entries = DatabasePrimitives.getFormattedPrimitivesFromDbMetadata()
        entries = if(attribute != null) {
            entries.filter {ce -> ce.key == attribute}.toSet()
        } else {
            entries
        }
        return Either.right(UIBeanArrayResult(connectionHeaders, entries))
    }
}