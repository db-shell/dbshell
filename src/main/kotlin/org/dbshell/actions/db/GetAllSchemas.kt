package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.db.metadata.DatabaseMetadata
import java.sql.DatabaseMetaData

data class GetAllSchemas(val dbmd: DatabaseMetaData): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any>> = mutableListOf(mutableMapOf("Schema" to "Schema"))
        val entries = DatabaseMetadata.getSchemas(dbmd)
        entries.forEach {entry ->
            values.add(mutableMapOf("Schema" to entry))
        }
        val data = values.map { v -> v.values.toTypedArray()}.toTypedArray()
        return Either.right(data)
    }
}