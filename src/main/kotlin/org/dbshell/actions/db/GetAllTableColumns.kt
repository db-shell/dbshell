package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.ActionResult
import org.dbshell.actions.UIAction
import org.dbshell.db.metadata.dto.Column

data class GetAllTableColumns(val tableName: String, val entries: List<Column>): UIAction() {
    override val headers: MutableSet<String>
        get() = mutableSetOf("Column Name", "Type Name", "Column Size", "Decimal Digits", "Precision", "Is Nullable",
            "Comments", "Default Value", "Is Primary Key", "Is Foreign Key", "Foreign Key Description")
    override fun execute(): ActionResult {
        val result = entries.map {e ->
            listOf(e.columnName, e.typeName, e.columnSize, e.decimalDigits, e.precision, e.isNullable, e.comments,
                e.defaultValue, e.isPk, e.isFk, e.fkDesc)
        }
        val gridResult = getGridResult(result)
        return Either.right(gridResult)
    }
}