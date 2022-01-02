package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.db.metadata.dto.Column

data class GetAllTableColumns(val tableName: String, val entries: List<Column>): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any?>> =
            mutableListOf(
                mutableMapOf(
                    "Column Name" to "Column Name",
                    "Type Name" to "Type Name",
                    "Column Size" to "Column Size",
                    "Decimal Digits" to "Decimal Digits",
                    "Precision" to "Precision",
                    "Is Nullable" to "Is Nullable",
                    "Comments" to "Comments",
                    "Default Value" to "Default Value",
                    "Is Primary Key" to "Is Primary Key",
                    "Is Foreign Key" to "Is Foreign Key",
                    "Foreign Key Description" to "Foreign Key Description"
                )
            )

        entries.forEach {entry ->
            values.add(
                mutableMapOf(
                    "Column Name" to entry.columnName,
                    "Type Name" to entry.typeName,
                    "Column Size" to entry.columnSize,
                    "Decimal Digits" to entry.decimalDigits,
                    "Precision" to entry.precision,
                    "Is Nullable" to entry.isNullable,
                    "Comments" to entry.comments,
                    "Default Value" to entry.defaultValue,
                    "Is Primary Key" to entry.isPk,
                    "Is Foreign Key" to entry.isFk,
                    "Foreign Key Description" to entry.fkDesc
                )
            )
        }
        val data = values.map { v -> v.values.toTypedArray()}.toTypedArray()
        return Either.right(data)
    }
}