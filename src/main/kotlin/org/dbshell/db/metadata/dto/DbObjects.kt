package org.dbshell.db.metadata.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
data class Schema(val schemaName: String) {
    override fun toString(): String {
        return schemaName
    }
}
data class Catalog(val catalogName: String) {
    override fun toString(): String {
        return catalogName
    }
}
data class Table(val tableName: String, val tableType: String)
data class Column(
    val columnName: String,
    val typeName: String,
    val columnSize: Int,
    val decimalDigits: Int,
    val precision: Int,
    val isNullable: Boolean,
    val comments: String?,
    val defaultValue: String?,
    val isPk: Boolean,
    val isFk: Boolean,
    val fkDesc: String
)