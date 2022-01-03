package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.GridResult
import org.dbshell.db.metadata.dto.Column

data class GetAllTableColumns(val tableName: String, val entries: List<Column>): Action {
    override fun execute(): ActionResult {
        val headers = setOf("Column Name", "Type Name", "Column Size", "Decimal Digits", "Precision", "Is Nullable",
            "Comments", "Default Value", "Is Primary Key", "Is Foreign Key", "Foreign Key Description")

        val result = entries.map {e ->
            listOf(e.columnName, e.typeName, e.columnSize, e.decimalDigits, e.precision, e.isNullable, e.comments,
                e.defaultValue, e.isPk, e.isFk, e.fkDesc)
        }
        val gridResult = GridResult(headers, result)
        return Either.right(gridResult)
    }
}