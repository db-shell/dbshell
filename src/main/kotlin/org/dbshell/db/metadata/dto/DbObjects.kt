package org.dbshell.db.metadata.dto

data class Schema(val schemaName: String)
data class Catalog(val catalogName: String)
data class Table(val tableName: String, val tableType: String)
data class Column(
    val columnName: String,
    val typeName: String,
    val columnSize: Int,
    val decimalDigits: Int,
    val precision: Int,
    val isNullable: Boolean,
    val comments: String,
    val defaultValue: String,
    val isPk: Boolean,
    val isFk: Boolean,
    val fkDesc: String
)