package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.db.metadata.DatabaseMetadata
import java.sql.DatabaseMetaData

data class GetAllCatalogs(val dbmd: DatabaseMetaData): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any>> = mutableListOf(mutableMapOf("Catalog" to "Catalog"))
        val entries = DatabaseMetadata.getCatalogs(dbmd)
        entries.forEach {entry ->
            values.add(mutableMapOf("Catalog" to entry))
        }
        val data = values.map { v -> v.values.toTypedArray()}.toTypedArray()
        return Either.right(data)
    }
}