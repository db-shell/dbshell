package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.db.metadata.DatabaseMetadata
import org.dbshell.environment.EnvironmentVars
import java.sql.DatabaseMetaData

data class GetAllTables(val dbmd: DatabaseMetaData, val includeViews: Boolean = false, val includeAll: Boolean = false): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any>> = mutableListOf(mutableMapOf("Table Name" to "Table Name", "Table Type" to "Table Type"))
        val types = mutableSetOf("TABLE")

        if(includeViews)
            types += "VIEW"

        if(includeAll) {
            types += "SYSTEM TABLE"
            types += "MATERIALIZED QUERY TABLE"
            types += "ALIAS"
        }
        val currentCatalog = EnvironmentVars.currentCatalog
        val currentSchema = EnvironmentVars.currentSchema
        val entries = DatabaseMetadata.getTables(dbmd, currentCatalog!!, currentSchema!!, types.toTypedArray())
        entries.sortBy {t -> t.tableName}
        entries.forEach {entry ->
            values.add(mutableMapOf("Table Name" to entry.tableName, "Table Type" to entry.tableType))
        }
        val data = values.map { v -> v.values.toTypedArray()}.toTypedArray()
        return Either.right(data)
    }
}